import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of, Subject } from 'rxjs';
import {filter, map, take, takeUntil} from 'rxjs/operators';
import { Court, UnknownCourt } from '../../../models/court.model';
import { AuthService } from '../../../services/auth.service';
import { CourtService } from '../../../services/court.service';
import { EventService } from '../../../services/event.service';
import { Event, EventType } from '../../../models/event.model';
import { MatDialog } from '@angular/material/dialog';
import { BookingFormComponent } from '../../../components/booking-form/booking-form.component';
import { UnknownBooking } from '../../../models/booking.model';
import { UserService } from '../../../services/user.service';
import { User } from '../../../models/user.model';
import { BookingService } from '../../../services/booking.service';
import {LessonFormComponent} from "../../../components/lesson-form/lesson-form.component";
import {LessonService} from "../../../services/lesson.service";
import {TournamentFormComponent} from "../../../components/tournament-form/tournament-form.component";
import {TournamentService} from "../../../services/tournament.service";

@Component({
  selector: 'tc-court-detail',
  templateUrl: './court-detail.component.html',
  styleUrls: ['./court-detail.component.scss'],
})
export class CourtDetailComponent implements OnInit, OnDestroy {
  displayedCourt$: Observable<Court | null> = of(null);
  displayedEventsAll$: Observable<Event[]> = of([]);
  displayedEventsFuture$: Observable<Event[]> = of([]);
  displayedEventsToday$: Observable<Event[]> = of([]);

  readonly userIsManager$ = this.auth.userIsManager$;

  private readonly unsubscribe$ = new Subject<void>();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly auth: AuthService,
    private readonly courtService: CourtService,
    private readonly eventService: EventService,
    private readonly userService: UserService,
    private readonly bookingService: BookingService,
    private readonly lessonService: LessonService,
    private readonly tournamentService: TournamentService,
    private readonly dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.unsubscribe$)).subscribe(({ id }) => {
      this.courtService.getCourtById(id);
      this.displayedCourt$ = this.courtService.singleCourt$(id);

      let today = new Date();
      this.eventService.getCourtEvents(id);
      this.displayedEventsAll$ = this.eventService.courtEvents$(id);
      this.displayedEventsFuture$ = this.eventService.courtEvents$(id).pipe(
          map(events => events.filter(event => new Date(event.endTime) >= today))
      );
      this.displayedEventsToday$ = this.eventService.courtEvents$(id).pipe(
          map(events => events.filter(event =>
            today.getDate() === new Date(event.startTime).getDate()
          ))
      )
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

  addLesson(court$: Observable<Court | null>): void {
    const dialog = this.dialog.open(LessonFormComponent, {
      disableClose: true,
      width: '50%',
    });
    dialog.componentInstance.submitButtonText = 'Create lesson';
    dialog.componentInstance.court$ = court$;

    dialog.componentInstance.lessonChange
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe(lesson => {
          this.lessonService.createLesson(lesson);
          dialog.close();
        });

    dialog.componentInstance.cancelClick
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe(() => {
          dialog.close();
        });
  }

  addTournament(court$: Observable<Court | null>): void {
    const dialog = this.dialog.open(TournamentFormComponent, {
      disableClose: true,
      width: '50%',
    });
    dialog.componentInstance.submitButtonText = 'Create tournament';
    dialog.componentInstance.court$ = court$;

    dialog.componentInstance.tournamentChange
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe(tournament => {
          this.tournamentService.createTournament(tournament);
          dialog.close();
        });

    dialog.componentInstance.cancelClick
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe(() => {
          dialog.close();
        });
  }
}
