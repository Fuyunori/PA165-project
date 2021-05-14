import { Component, Input } from '@angular/core';
import { Court } from 'src/app/models/court.model';

@Component({
  selector: 'tc-court-card',
  templateUrl: './court-card.component.html',
  styleUrls: ['./court-card.component.scss'],
})
export class CourtCardComponent {
  @Input() court?: Court;
  @Input() courtDetailLink?: string;
}
