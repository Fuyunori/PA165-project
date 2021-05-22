import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Court, UnknownCourt } from '../../../models/court.model';
import { AuthService } from '../../../services/auth.service';
import { CourtService } from '../../../services/court.service';
import { EventService } from '../../../services/event.service';
import {Event, EventType} from '../../../models/event.model';
import {MatDialog} from "@angular/material/dialog";

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
  }

  private readonly unsubscribe$ = new Subject<void>();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly auth: AuthService,
    private readonly courtService: CourtService,
    private readonly eventService: EventService,
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

  addBooking(court: Court) {

  }
}
