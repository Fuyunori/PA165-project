import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {Booking} from "../models/booking.model";
import {BehaviorSubject, Observable} from "rxjs";
import {Lesson} from "../models/lesson.model";
import {map} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";
import {NotificationService} from "./notification.service";
import {EventService} from "./event.service";

const RESOURCE_URL = `${environment.apiBaseUrl}/bookings`;

type BookingState = {
  entities: Record<number, Booking>;
  orderedIds: number[];
};

@Injectable({
  providedIn: 'root'
})
export class BookingService {
  private readonly state$ = new BehaviorSubject<BookingState>({
    entities: {},
    orderedIds: [],
  });

  readonly singleBooking$ = (id: number): Observable<Booking | null> =>
      this.state$.pipe(map(({ entities }) => entities[id] ?? null));

  constructor(
      private readonly http: HttpClient,
      private readonly notification: NotificationService,
      private readonly eventService: EventService,
  ) {}

  getBookingById(id: number): void {
    this.http
        .get<Booking>(`${RESOURCE_URL}/${id}`)
        .subscribe((booking: Booking) => {
          const { entities, orderedIds } = this.state$.value;
          this.state$.next({
            entities: { ...entities, [booking.id]: booking },
            orderedIds,
          });
        });
  }

    deleteBooking(booking: Booking): void {
      let id = booking.id;
        this.http.delete(`${RESOURCE_URL}/${id}`).subscribe(() => {
            const { entities, orderedIds } = this.state$.value;
            this.state$.next({
                entities: Object.values(entities)
                    .filter(booking => booking.id !== id)
                    .reduce((acc, b) => ({ ...acc, [b.id]: b }), {}),
                orderedIds: orderedIds.filter(ordId => ordId !== id),
            });
            this.eventService.getCourtEvents(booking.court.id);
        });
    }
}
