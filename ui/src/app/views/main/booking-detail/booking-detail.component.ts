import {Component, OnDestroy, OnInit} from '@angular/core';
import {Observable, of, Subject} from "rxjs";
import {Lesson} from "../../../models/lesson.model";
import {Booking} from "../../../models/booking.model";
import {BookingService} from "../../../services/booking.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";
import {filter, map, takeUntil} from "rxjs/operators";

@Component({
  selector: 'tc-booking-detail',
  templateUrl: './booking-detail.component.html',
  styleUrls: ['./booking-detail.component.scss']
})
export class BookingDetailComponent implements OnInit, OnDestroy {
  displayedBooking$: Observable<Booking | null> = of(null);
  userIsAuthor$: Observable<Boolean> = this.displayedBooking$.pipe(
      filter(booking => {
        console.log(booking);
        return booking != null;
      }),
      map(booking => {
        return true;
      }),
  );

  private readonly unsubscribe$ = new Subject<void>();

  constructor(private readonly bookingService: BookingService,
              private readonly route: ActivatedRoute,
              private readonly router: Router,
              private readonly auth: AuthService,) {
  }

  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.unsubscribe$)).subscribe(({ id }) => {
      console.log('one');
      this.bookingService.getBookingById(id);
      this.displayedBooking$ = this.bookingService.singleBooking$(id);
      console.log('two');
    });
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

}
