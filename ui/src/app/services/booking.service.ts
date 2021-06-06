import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Booking, UnknownBooking } from '../models/booking.model';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { NotificationService } from './notification.service';
import { EventService } from './event.service';

const RESOURCE_URL = `${environment.apiBaseUrl}/bookings`;

type BookingState = {
  entities: Record<number, Booking>;
  orderedIds: number[];
};

@Injectable({
  providedIn: 'root',
})
export class BookingService {
  private readonly state$ = new BehaviorSubject<BookingState>({
    entities: {},
    orderedIds: [],
  });

  readonly unorderedBookings$: Observable<Booking[]> = this.state$.pipe(
    map(({ entities, orderedIds }) => Object.values(entities)),
  );

  readonly singleBooking$ = (id: number): Observable<Booking | null> =>
    this.state$.pipe(map(({ entities }) => entities[id] ?? null));

  constructor(
    private readonly http: HttpClient,
    private readonly notification: NotificationService,
    private readonly eventService: EventService,
  ) {}

  getBookings(): void {
    this.http.get<Booking[]>(RESOURCE_URL).subscribe(bookings => {
      this.state$.next({
        entities: bookings.reduce((acc, c) => ({ ...acc, [c.id]: c }), {}),
        orderedIds: bookings.map(({ id }) => id),
      });
    });
  }

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

  postBooking(booking: UnknownBooking): void {
    this.http.post<Booking>(RESOURCE_URL, booking).subscribe(
      resBooking => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resBooking.id]: resBooking },
          orderedIds: [...orderedIds, resBooking.id],
        });
        this.eventService.getCourtEvents(resBooking.court.id);
        this.notification.toastSuccess('Booking created!');
      },
      err => {
        if (err.status !== 0) {
          if (typeof err.error === 'string') {
            this.notification.toastError(err.error);
          } else {
            this.notification.toastError('Could not create booking!');
          }
        }
      },
    );
  }

  putBooking(booking: Booking): void {
    this.http
      .put<Booking>(`${RESOURCE_URL}/${booking.id}`, booking)
      .pipe(this.notification.onError('Could not update booking!'))
      .subscribe(resBooking => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resBooking.id]: resBooking },
          orderedIds,
        });
        this.eventService.getCourtEvents(booking.court.id);
        this.notification.toastSuccess('Booking updated!');
      });
  }
}
