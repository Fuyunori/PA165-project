import {Component, Input, OnInit} from '@angular/core';
import {Court} from "../../models/court.model";

enum CourtFormKey {
  Date = 'Name',
  From = 'Address',
  To = 'Type',
}

@Component({
  selector: 'tc-booking-form',
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.scss']
})
export class BookingFormComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
