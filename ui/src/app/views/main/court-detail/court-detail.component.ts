import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Court, UnknownCourt } from '../../../models/court.model';
import { AuthService } from '../../../services/auth.service';
import { CourtService } from '../../../services/court.service';

@Component({
  selector: 'tc-court-detail',
  templateUrl: './court-detail.component.html',
  styleUrls: ['./court-detail.component.scss'],
})
export class CourtDetailComponent implements OnInit, OnDestroy {
  displayedCourt$: Observable<Court | null> = of(null);

  readonly userIsManager$ = this.auth.userIsManager$;

  private readonly unsubscribe$ = new Subject<void>();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly auth: AuthService,
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

  updateCourt(displayedCourt: Court, updatedCourt: UnknownCourt): void {
    this.courtService.putCourt(displayedCourt.id, updatedCourt);
  }

  deleteCourt(displayedCourt: Court): void {
    if (confirm(`Permanently delete court ${displayedCourt.name}?`)) {
      this.courtService.deleteCourt(displayedCourt.id);
      this.router.navigateByUrl('/main/dashboard');
    }
  }
}
