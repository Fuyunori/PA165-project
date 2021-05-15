import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Court, UnknownCourt } from '../models/court.model';
import { map } from 'rxjs/operators';

const RESOURCE_URL = `${environment.apiBaseUrl}/courts`;

type CourtsState = {
  entities: Record<string, Court>;
  orderedIds: string[];
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

  readonly singleCourt$ = (id: string): Observable<Court | null> =>
    this.state$.pipe(map(({ entities }) => entities[id] ?? null));

  constructor(private readonly http: HttpClient) {}

  getCourts(): void {
    this.http.get<Court[]>(RESOURCE_URL).subscribe(courts => {
      this.state$.next({
        entities: courts.reduce((acc, c) => ({ ...acc, [c.id]: c }), {}),
        orderedIds: courts.map(({ id }) => id),
      });
    });
  }

  getCourtById(id: string) {
    this.http.get<Court>(`${RESOURCE_URL}/${id}`).subscribe(court => {
      const { entities, orderedIds } = this.state$.value;
      this.state$.next({
        entities: { ...entities, [court.id]: court },
        orderedIds,
      });
    });
  }

  postCourt(court: UnknownCourt): void {
    this.http.post<Court>(RESOURCE_URL, court).subscribe(resCourt => {
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

  deleteCourt(court: Court): void {
    this.http.delete(`${RESOURCE_URL}/${court.id}`).subscribe(() => {
      const { entities, orderedIds } = this.state$.value;
      this.state$.next({
        entities: Object.values(entities)
          .filter(({ id }) => id !== court.id)
          .reduce((acc, c) => ({ ...acc, [c.id]: c }), {}),
        orderedIds: orderedIds.filter(id => id !== court.id),
      });
    });
  }
}
