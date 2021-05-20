import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Event } from '../models/event.model';
import { environment } from '../../environments/environment';

const RESOURCE_URL = (id: number) =>
  `${environment.apiBaseUrl}/courts/${id}/events`;

type EventsState = {
  entities: Record<number, Event>;
  eventIdsByCourt: Record<number, number[]>;
};

@Injectable({ providedIn: 'root' })
export class EventService {
  private readonly state$ = new BehaviorSubject<EventsState>({
    entities: {},
    eventIdsByCourt: {},
  });

  readonly courtEvents$ = (courtId: number): Observable<Event[]> =>
    this.state$.pipe(
      map(
        ({ entities, eventIdsByCourt }) =>
          eventIdsByCourt[courtId]?.map(id => entities[id]) ?? [],
      ),
    );

  constructor(private readonly http: HttpClient) {}

  getCourtEvents(courtId: number): void {
    this.http.get<Event[]>(RESOURCE_URL(courtId)).subscribe(events => {
      const { entities, eventIdsByCourt } = this.state$.value;
      this.state$.next({
        entities: events.reduce((acc, c) => ({ ...acc, [c.id]: c }), entities),
        eventIdsByCourt: {
          ...eventIdsByCourt,
          [courtId]: events.map(({ id }) => id),
        },
      });
    });
  }
}
