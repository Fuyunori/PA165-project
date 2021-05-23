import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Court } from '../../models/court.model';
import {AbstractControl, FormBuilder, Validators} from '@angular/forms';
import { CourtService } from '../../services/court.service';
import {
  Booking,
  FormBooking,
} from '../../models/booking.model';
import { EventType } from '../../models/event.model';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';
import { NotificationService } from '../../services/notification.service';
import {AuthService} from "../../services/auth.service";

enum BookingFormKey {
  Court = 'Court',
  Start = 'Name',
  End = 'Address',
  User = 'User',
}

@Component({
  selector: 'tc-booking-form',
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.scss'],
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
    });
  }

  @Input()
  set booking(booking: Booking) {
    this.bookingForm.setValue({
      [BookingFormKey.Start]: booking.startTime,
      [BookingFormKey.End]: booking.endTime,
      [BookingFormKey.User]: '',
    });
    this.authorUsername = booking.author.username;
    this.courtName = booking.court.name;
    this.selectedUsers = booking.users;
  }

  authorUsername: string = '';
  courtName: string = '';
  selectedUsers: User[] = [];
  usersChanged = false;

  readonly bookingForm = this.fb.group({
    [BookingFormKey.Start]: ['', Validators.required],
    [BookingFormKey.End]: ['', Validators.required],
    [BookingFormKey.User]: '',
  });

  readonly BookingFormKey = BookingFormKey;

  constructor(
    private readonly fb: FormBuilder,
    private readonly courtService: CourtService,
    private readonly userService: UserService,
    private readonly auth: AuthService,
    private readonly notification: NotificationService,
  ) {}

  ngOnInit(): void {
    this.bookingForm.controls[BookingFormKey.Start].setValidators([this.isInFutureValidator, this.isBeforeEndValidator]);
    this.bookingForm.controls[BookingFormKey.End].setValidators([this.isInFutureValidator, this.isAfterStartValidator]);
  }

  isInFutureValidator = (form: AbstractControl) => {
    let formDate = new Date(form.value);
    let today = new Date();

    if(formDate < today){
      return { error: "Lol, what are you doing. The date must be in the future."};
    }
    return null;
  }

  isAfterStartValidator = (form: AbstractControl) => {
    let formDate = new Date(form.value);
    let startDate = new Date(this.bookingForm.controls[BookingFormKey.Start].value);

    if(formDate < startDate){
      return { error: "Bro, the end date must be after the start date."};
    }
    return null;
  }

  isBeforeEndValidator = (form: AbstractControl) => {
    let formDate = new Date(form.value);
    let endDate = new Date(this.bookingForm.controls[BookingFormKey.End].value);

    if(formDate > endDate){
      return { error: "Start date must be before the end date."};
    }
    return null;
  }

  addUser(): void {
    let username = this.bookingForm.value[BookingFormKey.User];
    if (
      !username ||
      this.selectedUsers.map(u => u.username).includes(username)
    ) {
      return;
    }
    this.userService.getUserByUsername(username).subscribe(
      user => {
        if (!user) {
          this.notification.toastError(`Could not find user: ${username}`);
          return;
        }
        this.usersChanged = true;
        this.selectedUsers.push(user);
        this.bookingForm.patchValue({
          [BookingFormKey.User]: '',
        });
      },
      err => {
        this.notification.toastError(`Could not find user: ${username}`);
      },
    );
  }

  deleteUserItem(username: string) {
    if (
      !username ||
      !this.selectedUsers.map(u => u.username).includes(username)
    ) {
      return;
    }
    this.usersChanged = true;
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

    this.submitClick.emit(booking);
  }

  cancel(): void {
    this.cancelClick.emit();
  }
}
