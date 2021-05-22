import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of, Subject } from 'rxjs';
import { filter, take, takeUntil } from 'rxjs/operators';
import { Court, UnknownCourt } from '../../../models/court.model';
import { AuthService } from '../../../services/auth.service';
import { CourtService } from '../../../services/court.service';
import { EventService } from '../../../services/event.service';
import { Event, EventType } from '../../../models/event.model';
import { MatDialog } from '@angular/material/dialog';
import { BookingFormComponent } from '../../../components/booking-form/booking-form.component';
import { Booking, UnknownBooking } from '../../../models/booking.model';
import { UserService } from '../../../services/user.service';
import { User } from '../../../models/user.model';
import { BookingService } from '../../../services/booking.service';
import { NotificationService } from '../../../services/notification.service';

enum EventTableColumn {
  Type = 'Type',
  StartTime = 'StartTime',
  EndTime = 'EndTime',
}

@Component({
  selector: 'tc-court-detail',
  templateUrl: './court-detail.component.html',
  styleUrls: ['./court-detail.component.scss'],
})
export class CourtDetailComponent implements OnInit, OnDestroy {
  displayedCourt$: Observable<Court | null> = of(null);
  displayedEvents$: Observable<Event[]> = of([]);

  readonly userIsManager$ = this.auth.userIsManager$;

  readonly EventTableColumn = EventTableColumn;
  readonly eventColumns: EventTableColumn[] = [
    EventTableColumn.Type,
    EventTableColumn.StartTime,
    EventTableColumn.EndTime,
  ];

  readonly typeNames: Record<EventType, string> = {
    LESSON: 'Lesson',
    BOOKING: 'Booking',
    TOURNAMENT: 'Tournament',
  };

  private readonly unsubscribe$ = new Subject<void>();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly auth: AuthService,
    private readonly courtService: CourtService,
    private readonly eventService: EventService,
    private readonly userService: UserService,
    private readonly bookingService: BookingService,
    private readonly dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.unsubscribe$)).subscribe(({ id }) => {
      this.courtService.getCourtById(id);
      this.displayedCourt$ = this.courtService.singleCourt$(id);

      this.eventService.getCourtEvents(id);
      this.displayedEvents$ = this.eventService.courtEvents$(id);
    });
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  updateCourt(displayedCourt: Court, updatedCourt: UnknownCourt): void {
    this.courtService.putCourt(displayedCourt.id, updatedCourt);
  }

  deleteCourt(displayedCourt: Court): void {
    if (confirm(`Permanently delete court ${displayedCourt.name}?`)) {
      this.courtService.deleteCourt(displayedCourt.id);
      this.router.navigateByUrl('/main/dashboard');
    }
  }

  asEventType(type: any): EventType {
    return type;
  }

  addBooking(court: Court): void {
    const dialog = this.dialog.open(BookingFormComponent, {
      disableClose: true,
      width: '50%',
    });
    dialog.componentInstance.submitButtonText = 'Make booking';
    dialog.componentInstance.court = court;

    dialog.componentInstance.submitClick
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(formBooking => {
        this.auth.userId$
          .pipe(
            filter((id): id is number => id != null),
            take(1),
          )
          .subscribe(userId => {
            this.userService.getUserById(userId);
            this.userService
              .singleUser$(userId)
              .pipe(
                filter((u): u is User => u != null),
                take(1),
              )
              .subscribe(user => {
                let booking: UnknownBooking = {
                  type: EventType.Booking,
                  court: court,
                  startTime: formBooking.startTime,
                  endTime: formBooking.endTime,
                  users: formBooking.users,
                  author: {
                    id: user.id,
                    name: user.name,
                    email: user.email,
                    username: user.username,
                    role: user.role,
                  },
                };
                this.bookingService.postBooking(booking);
                dialog.close();
              });
          });
      });

    dialog.componentInstance.cancelClick
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(() => {
        dialog.close();
      });
  }
}
