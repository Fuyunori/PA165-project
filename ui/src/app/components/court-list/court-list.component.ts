import { Component, OnInit } from '@angular/core';
import { CourtService } from 'src/app/services/court.service';
import { MatDialog } from '@angular/material/dialog';
import { CourtFormComponent } from '../court-form/court-form.component';
import { take } from 'rxjs/operators';

@Component({
  selector: 'tc-court-list',
  templateUrl: './court-list.component.html',
  styleUrls: ['./court-list.component.scss'],
})
export class CourtListComponent implements OnInit {
  readonly courts$ = this.courtService.orderedCourts$;

  constructor(
    private readonly courtService: CourtService,
    private readonly dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.courtService.getCourts();
  }

  addCourt(): void {
    const dialog = this.dialog.open(CourtFormComponent, {});
    dialog.componentInstance.submitButtonText = 'Add court';

    dialog.componentInstance.courtChange.pipe(take(1)).subscribe(court => {
      this.courtService.postCourt(court);
      dialog.close();
    });
  }
}
