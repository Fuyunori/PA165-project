import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Court, UnknownCourt} from "../../models/court.model";
import {FormBuilder, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {CourtService} from "../../services/court.service";
import {Booking, UnknownBooking} from "../../models/booking.model";
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
  @Output() readonly submitClick = new EventEmitter<UnknownCourt>();

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
    this.selectedUsers = booking.users.map(u => u.username);
  }

  authorUsername: string = '';
  courtName: string = '';
  readonly courts$: Observable<Court[]> = this.courtService.orderedCourts$;
  readonly users$: Observable<User[]> = this.userService.orderedUsers$;
  selectedUsers: string[] = [];


  readonly bookingForm = this.fb.group({
    [BookingFormKey.Court]: ['', Validators.required],
    [BookingFormKey.Start]: ['', Validators.required],
    [BookingFormKey.End]: ['', Validators.required],
    [BookingFormKey.User]: '',
    [BookingFormKey.Author]: ['', Validators.required],
  });

  readonly BookingFormKey = BookingFormKey;

  constructor(private readonly fb: FormBuilder,
              private readonly courtService: CourtService,
              private readonly userService: UserService) {
}

  ngOnInit(): void {
    this.courtService.getCourts();
    this.userService.getUsers();
  }

  addUser(): void {
    let username = this.bookingForm.value[BookingFormKey.User];
    if (!username || this.selectedUsers.includes(username)) {
      return;
    }
    this.selectedUsers.push(username);
    /*let id: number = this.bookingForm.value[BookingFormKey.User];
    if (this.selectedUsers.map(u => u.id).includes(id)) {
      return;
    }
    if (id && !this.selectedUsers.map(user => user.id).includes(id)) {
      this.userService.getUserById(id);
      this.userService.singleUser$(id).pipe(
          take(1),
          filter((user):user is User => user != null),
      ).subscribe(user => {
        this.selectedUsers.push(user);
      });
    }*/
  }

  deleteUserItem(username: string) {
    this.selectedUsers = this.selectedUsers.filter(u => u != username);
    //this.selectedUsers = this.selectedUsers.filter(u => u.id != id);
  }

  submit(): void {
    const { value } = this.bookingForm;

    this.courtService.getCourtById(value[BookingFormKey.Court]);
    this.courtService.singleCourt$(value[BookingFormKey.Court]).pipe(
      take(1),
      filter((court):court is Court => court != null),
    ).subscribe(court => {
           /* const booking: UnknownBooking = {
              type: EventType.Booking,
              court,
              startTime: value[BookingFormKey.Start],
              endTime: value[BookingFormKey.End],
              author: this.author,
              users: this.selectedUsers,
            };

          console.log(booking);*/
    });

    console.log(this.bookingForm.value[BookingFormKey.Court]?.address);
  }

  cancel(): void {
    this.cancelClick.emit();
  }

}
