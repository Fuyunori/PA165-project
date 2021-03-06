import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { CourtService } from '../../services/court.service';
import { Booking, FormBooking } from '../../models/booking.model';
import { EventType } from '../../models/event.model';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';
import { NotificationService } from '../../services/notification.service';
import { AuthService } from '../../services/auth.service';

enum BookingFormKey {
  Start = 'Start',
  End = 'End',
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

  private isOver = false;

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
    if (new Date(booking.startTime) < new Date()) {
      this.isOver = true;
    }
  }

  authorUsername: string = '';
  courtName: string = '';
  selectedUsers: User[] = [];
  usersChanged = false;
  calledFromOtherValidator = false;

  readonly bookingForm = this.fb.group({
    [BookingFormKey.Start]: '',
    [BookingFormKey.End]: '',
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
    this.bookingForm.controls[BookingFormKey.Start].setValidators([
      Validators.required,
      this.isInFutureValidator,
      this.isBeforeEndValidator,
    ]);
    this.bookingForm.controls[BookingFormKey.End].setValidators([
      Validators.required,
      this.isInFutureValidator,
      this.isAfterStartValidator,
    ]);
  }

  isReadOnly() {
    return this.readOnly || this.isOver;
  }

  isInFutureValidator = (form: AbstractControl) => {
    let formDate = new Date(form.value);
    let today = new Date();

    if (!this.isReadOnly() && formDate < today) {
      return {
        error: 'Lol, what are you doing. The date must be in the future.',
      };
    }
    return null;
  };

  isAfterStartValidator = (form: AbstractControl) => {
    let formDate = new Date(form.value);
    let startDate = new Date(
      this.bookingForm.controls[BookingFormKey.Start].value,
    );

    if (!this.calledFromOtherValidator) {
      this.calledFromOtherValidator = true;
      this.bookingForm.controls[BookingFormKey.Start].updateValueAndValidity();
      this.calledFromOtherValidator = false;
    }

    if (!this.isReadOnly() && formDate < startDate) {
      return { error: 'Bro, the end date must be after the start date.' };
    }

    return null;
  };

  isBeforeEndValidator = (form: AbstractControl) => {
    let formDate = new Date(form.value);
    let endDate = new Date(this.bookingForm.controls[BookingFormKey.End].value);

    if (!this.calledFromOtherValidator) {
      this.calledFromOtherValidator = true;
      this.bookingForm.controls[BookingFormKey.End].updateValueAndValidity();
      this.calledFromOtherValidator = false;
    }

    if (!this.isReadOnly() && formDate > endDate) {
      return { error: 'Start date must be before the end date.' };
    }

    return null;
  };

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
      _ => {
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
