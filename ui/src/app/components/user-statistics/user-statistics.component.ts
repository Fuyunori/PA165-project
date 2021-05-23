import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../models/user.model";
import {ActivatedRoute} from "@angular/router";
import {filter, map, reduce, takeUntil} from "rxjs/operators";
import {Observable, of, Subject} from "rxjs";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";
import {BookingService} from "../../services/booking.service";
import {Booking} from "../../models/booking.model";

@Component({
  selector: 'tc-user-statistics',
  templateUrl: './user-statistics.component.html',
  styleUrls: ['./user-statistics.component.scss']
})
export class UserStatisticsComponent implements OnInit {
  private usersBookings$: Observable<Booking[]> = of([]);

  hoursLastWeek$: Observable<number> = of(0);
  hoursLastMonth$: Observable<number> = of(0);
  hoursLastYear$: Observable<number> = of(0);

  private readonly unsubscribe$ = new Subject<void>();

  constructor(private readonly route: ActivatedRoute,
              private readonly auth: AuthService,
              private readonly userService: UserService,
              private readonly bookingService: BookingService) { }

  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.unsubscribe$)).subscribe(({ id }) => {
      console.log(id);
      this.bookingService.getBookings();
      this.usersBookings$ = this.bookingService.unorderedBookings$.pipe(
          filter(bookings => bookings?.length > 0),
          map((bookings) => bookings.filter(booking =>
              booking.author.id == id
            )
          ),
          map(bookings => bookings.map(booking => {
                booking.startTime = new Date(booking.startTime);
                booking.endTime = new Date(booking.endTime);
                return booking;
              }
          ))
      )

      let today = new Date();
      let lastWeek = new Date(today.getTime() - 7 * 24 * 60 * 60 * 1000);
      let lastMonth = new Date(today.getTime() - 31 * 24 * 60 * 60 * 1000);
      let lastYear = new Date(today.getTime() - 365 * 24 * 60 * 60 * 1000);

      this.hoursLastWeek$ = this.hoursInInterval(lastWeek, today);
      this.hoursLastMonth$ = this.hoursInInterval(lastMonth, today);
      this.hoursLastYear$ = this.hoursInInterval(lastYear, today);
    });
  }

  hoursInInterval(from: Date, to: Date): Observable<number> {
    return this.usersBookings$.pipe(
      map(bookings => bookings.filter(booking =>
          booking.startTime >= from && booking.endTime <= to
          )
      ),
      map(bookings => bookings.reduce(
          (acc, booking) => {
            return acc + booking.endTime.getTime() - booking.startTime.getTime()
          },
          0
      )),
      map(time => time/(1000*60*60))
    );
  }

}
