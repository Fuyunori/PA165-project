import {Component, OnDestroy, OnInit} from '@angular/core';
import {Observable, of, Subject, zip} from "rxjs";
import {Booking} from "../../../models/booking.model";
import {BookingService} from "../../../services/booking.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";
import {filter, map, take, takeUntil} from "rxjs/operators";

@Component({
  selector: 'tc-booking-detail',
  templateUrl: './booking-detail.component.html',
  styleUrls: ['./booking-detail.component.scss']
})
export class BookingDetailComponent implements OnInit, OnDestroy {
  displayedBooking$: Observable<Booking | null> = of(null);
  userIsAuthor$: Observable<Boolean | null> = of(null);

  private readonly unsubscribe$ = new Subject<void>();

  constructor(private readonly bookingService: BookingService,
              private readonly route: ActivatedRoute,
              private readonly router: Router,
              private readonly auth: AuthService,) {
  }

  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.unsubscribe$)).subscribe(({ id }) => {
      this.bookingService.getBookingById(id);
      this.displayedBooking$ = this.bookingService.singleBooking$(id);
      this.userIsAuthor$ = zip(this.displayedBooking$, this.auth.userId$).pipe(
          filter(([booking, id]) => booking != null && id != null),
          take(1),
          map(([booking, id]) => booking?.author.id === id)
      );
    });
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  deleteBooking(booking: Booking): void {
    if (confirm(`Permanently delete booking?`)) {
      this.bookingService.deleteBooking(booking);
      this.router.navigateByUrl(`/main/court/${booking.court.id}`);
    }
  }

  updateBooking(): void {

  }
}
