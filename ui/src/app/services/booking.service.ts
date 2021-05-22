import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {Booking} from "../models/booking.model";
import {BehaviorSubject, Observable} from "rxjs";
import {Lesson} from "../models/lesson.model";
import {map} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";
import {NotificationService} from "./notification.service";

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
}
