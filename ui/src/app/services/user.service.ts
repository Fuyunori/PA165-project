import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User, UnknownUser } from '../models/user.model';
import { map } from 'rxjs/operators';
import { NotificationService } from './notification.service';

const RESOURCE_URL = `${environment.apiBaseUrl}/users`;

type UsersState = {
  entities: Record<number, User>;
  orderedIds: number[];
};

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly state$ = new BehaviorSubject<UsersState>({
    entities: {},
    orderedIds: [],
  });

  readonly orderedUsers$: Observable<User[]> = this.state$.pipe(
    map(({ entities, orderedIds }) => orderedIds.map(id => entities[id])),
  );

  readonly singleUser$ = (id: number): Observable<User | null> =>
    this.state$.pipe(map(({ entities }) => entities[id] ?? null));

  constructor(
    private readonly http: HttpClient,
    private readonly notification: NotificationService,
  ) {}

  getUsers(): void {
    this.http.get<User[]>(RESOURCE_URL).subscribe(users => {
      this.state$.next({
        entities: users.reduce((acc, c) => ({ ...acc, [c.id]: c }), {}),
        orderedIds: users.map(({ id }) => id),
      });
    });
  }

  getUserById(id: number) {
    this.http.get<User>(`${RESOURCE_URL}/${id}`).subscribe(user => {
      const { entities, orderedIds } = this.state$.value;
      this.state$.next({
        entities: { ...entities, [user.id]: user },
        orderedIds,
      });
    });
  }

  getUserByUsername(username: string): Observable<User | null> {
    let queryParams: HttpParams = new HttpParams().set('username', username);
    return this.http
      .get<User[]>(`${RESOURCE_URL}/`, { params: queryParams })
      .pipe(
        map(users => {
          if (!users) {
            return null;
          }
          return users[0];
        }),
      );
  }

  postUser(user: UnknownUser): void {
    this.http
      .post<User>(RESOURCE_URL, user)
      .pipe(this.notification.onError('Could not register!'))
      .subscribe(resUser => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resUser.id]: resUser },
          orderedIds: [...orderedIds, resUser.id],
        });
      });
  }

  putUser(id: number, user: UnknownUser): void {
    this.http.put<User>(`${RESOURCE_URL}/${id}`, user).subscribe(resUser => {
      const { entities, orderedIds } = this.state$.value;
      this.state$.next({
        entities: { ...entities, [resUser.id]: resUser },
        orderedIds,
      });
    });
  }

  deleteUser(id: number): void {
    this.http.delete(`${RESOURCE_URL}/${id}`).subscribe(() => {
      const { entities, orderedIds } = this.state$.value;
      this.state$.next({
        entities: Object.values(entities)
          .filter(c => c.id !== id)
          .reduce((acc, c) => ({ ...acc, [c.id]: c }), {}),
        orderedIds: orderedIds.filter(ordId => ordId !== id),
      });
    });
  }
}
