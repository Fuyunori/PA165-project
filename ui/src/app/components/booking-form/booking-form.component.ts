import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Court, UnknownCourt} from "../../models/court.model";
import {FormBuilder, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {CourtService} from "../../services/court.service";
import {Booking, FormBooking, UnknownBooking} from "../../models/booking.model";
import {filter, take} from "rxjs/operators";
import {EventType} from "../../models/event.model";
import {UserService} from "../../services/user.service";
import {User} from "../../models/user.model";

enum BookingFormKey {
  Court = 'Court',
  Start = 'Name',
  End = 'Address',
  User = 'User',
  Author = 'Author',
}

@Component({
  selector: 'tc-booking-form',
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.scss']
})
export class BookingFormComponent implements OnInit {
  @Output() readonly cancelClick = new EventEmitter<void>();
  @Output() readonly submitClick = new EventEmitter<FormBooking>();

  @Input() readOnly = false;
  @Input() submitButtonText = 'Submit';
  @Input() cancelButtonText = 'Cancel';

  @Input()
  set court(court: Court) {
    this.bookingForm.patchValue({
      [BookingFormKey.Court]: court.id,
    })
  }

  @Input()
  set booking(booking: Booking) {
    this.bookingForm.setValue({
      [BookingFormKey.Court]: booking.court.id,
      [BookingFormKey.Start]: booking.startTime,
      [BookingFormKey.End]: booking.endTime,
      [BookingFormKey.User]: '',
      [BookingFormKey.Author]: booking.author.username,
    });
    this.authorUsername = booking.author.username;
    this.courtName = booking.court.name;
    this.selectedUsers = booking.users;
  }

  authorUsername: string = '';
  courtName: string = '';
  selectedUsers: User[] = [];


  readonly bookingForm = this.fb.group({
    [BookingFormKey.Start]: ['', Validators.required],
    [BookingFormKey.End]: ['', Validators.required],
    [BookingFormKey.User]: '',
  });

  readonly BookingFormKey = BookingFormKey;

  constructor(private readonly fb: FormBuilder,
              private readonly courtService: CourtService,
              private readonly userService: UserService) {
}

  ngOnInit(): void {
    this.courtService.getCourts();
  }

  addUser(): void {
    let username = this.bookingForm.value[BookingFormKey.User];
    if (!username || this.selectedUsers.map(u => u.username).includes(username)) {
      return;
    }
    this.userService.getUserByUsername(username).subscribe(
        (user) => {
          if (!user) {
            console.log("Error")
            return;
          }
        this.selectedUsers.push(user);
          this.bookingForm.patchValue({
            [BookingFormKey.User]: '',
          })
      },
        (err) => {
          console.log("Error")
        },
    );
  }

  deleteUserItem(username: string) {
    this.selectedUsers = this.selectedUsers.filter(u => u.username != username);
  }

  submit(): void {
    const { value } = this.bookingForm;

    const booking: FormBooking = {
      type: EventType.Booking,
      startTime: value[BookingFormKey.Start],
      endTime: value[BookingFormKey.End],
      users: this.selectedUsers,
    };

    this.bookingForm.markAsPristine();
    this.submitClick.emit(booking);
  }

  cancel(): void {
    this.cancelClick.emit();
  }

}
