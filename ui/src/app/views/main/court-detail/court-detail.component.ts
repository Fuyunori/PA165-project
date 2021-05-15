import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, of, Subject } from 'rxjs';
import { filter, take, takeUntil } from 'rxjs/operators';
import { Court, UnknownCourt } from 'src/app/models/court.model';
import { CourtService } from 'src/app/services/court.service';

@Component({
  selector: 'tc-court-detail',
  templateUrl: './court-detail.component.html',
  styleUrls: ['./court-detail.component.scss'],
})
export class CourtDetailComponent implements OnInit, OnDestroy {
  displayedCourt$: Observable<Court | null> = of(null);

  private readonly unsubscribe$ = new Subject<void>();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly courtService: CourtService,
  ) {}

  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.unsubscribe$)).subscribe(({ id }) => {
      this.courtService.getCourtById(id);
      this.displayedCourt$ = this.courtService.singleCourt$(id);
    });
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  updateCourt(updatedCourt: UnknownCourt): void {
    this.displayedCourt$
      .pipe(
        take(1),
        filter((court): court is Court => court != null),
      )
      .subscribe(displayedCourt => {
        this.courtService.putCourt(displayedCourt.id, updatedCourt);
      });
  }
}
