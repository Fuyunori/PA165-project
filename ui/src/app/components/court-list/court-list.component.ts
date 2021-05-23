import { Component, OnDestroy, OnInit } from '@angular/core';
import { CourtService } from '../../services/court.service';
import { MatDialog } from '@angular/material/dialog';
import { CourtFormComponent } from '../court-form/court-form.component';
import { map, take, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'tc-court-list',
  templateUrl: './court-list.component.html',
  styleUrls: ['./court-list.component.scss'],
})
export class CourtListComponent implements OnInit, OnDestroy {
  readonly userIsManager$ = this.auth.userIsManager$;
  readonly courts$ = this.courtService.orderedCourts$;

  private readonly unsubscribe$ = new Subject<void>();

  constructor(
    private readonly auth: AuthService,
    private readonly courtService: CourtService,
    private readonly dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.courtService.getCourts();
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  addCourt(): void {
    const dialog = this.dialog.open(CourtFormComponent, { disableClose: true });
    const form = dialog.componentInstance;

    form.submitButtonText = 'Add court';

    form.nameValidator = ({ value }) =>
      this.courts$.pipe(
        map(courts =>
          courts.some(({ name }) => name === value)
            ? { error: 'A court with this name already exists' }
            : null,
        ),
        take(1),
      );

    form.courtChange.pipe(takeUntil(this.unsubscribe$)).subscribe(court => {
      this.courtService.postCourt(court);
      dialog.close();
    });

    form.cancelClick.pipe(takeUntil(this.unsubscribe$)).subscribe(() => {
      dialog.close();
    });
  }
}
