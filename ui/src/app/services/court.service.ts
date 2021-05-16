import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Court, UnknownCourt } from '../models/court.model';
import { map } from 'rxjs/operators';
import { NotificationService } from './notification.service';

const RESOURCE_URL = `${environment.apiBaseUrl}/courts`;

type CourtsState = {
  entities: Record<number, Court>;
  orderedIds: number[];
};

@Injectable({ providedIn: 'root' })
export class CourtService {
  private readonly state$ = new BehaviorSubject<CourtsState>({
    entities: {},
    orderedIds: [],
  });

  readonly orderedCourts$: Observable<Court[]> = this.state$.pipe(
    map(({ entities, orderedIds }) => orderedIds.map(id => entities[id])),
  );

  readonly singleCourt$ = (id: number): Observable<Court | null> =>
    this.state$.pipe(map(({ entities }) => entities[id] ?? null));

  constructor(
    private readonly http: HttpClient,
    private readonly notification: NotificationService,
  ) {}

  getCourts(): void {
    this.http.get<Court[]>(RESOURCE_URL).subscribe(courts => {
      this.state$.next({
        entities: courts.reduce((acc, c) => ({ ...acc, [c.id]: c }), {}),
        orderedIds: courts.map(({ id }) => id),
      });
    });
  }

  getCourtById(id: number) {
    this.http.get<Court>(`${RESOURCE_URL}/${id}`).subscribe(court => {
      const { entities, orderedIds } = this.state$.value;
      this.state$.next({
        entities: { ...entities, [court.id]: court },
        orderedIds,
      });
    });
  }

  postCourt(court: UnknownCourt): void {
    this.http
      .post<Court>(RESOURCE_URL, court)
      .pipe(this.notification.onError('Could not add court!'))
      .subscribe(resCourt => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resCourt.id]: resCourt },
          orderedIds: [...orderedIds, resCourt.id],
        });
      });
  }

  putCourt(id: string, court: UnknownCourt): void {
    this.http.put<Court>(`${RESOURCE_URL}/${id}`, court).subscribe(resCourt => {
      const { entities, orderedIds } = this.state$.value;
      this.state$.next({
        entities: { ...entities, [resCourt.id]: resCourt },
        orderedIds,
      });
    });
  }

  deleteCourt(id: number): void {
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
