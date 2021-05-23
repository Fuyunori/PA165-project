import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {User} from "../../models/user.model";
import {Observable} from "rxjs";
import {FormControl} from "@angular/forms";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'tc-enroll-student-view',
  templateUrl: './add-user-view.component.html',
  styleUrls: ['./add-user-view.component.scss'],
})

export class AddUserViewComponent implements OnInit {
  @Input()
  usersToExcludePrimary: User[] = [];

  @Input()
  usersToExcludeSecondary: User[] = [];

  @Input()
  actionText: string = '';

  @Input()
  excludedTextPrimary: string = '';

  @Input()
  excludedTextSecondary: string = '';

  @Output() readonly cancelClick = new EventEmitter<void>();
  @Output() readonly selectedUser = new EventEmitter<User>();

  users$: Observable<User[]> = this.userService.orderedUsers$;
  userNameFormControl = new FormControl('');

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getUsers();
  }

  isExcludedPrimary(user: User): boolean {
    return this.usersToExcludePrimary.some((elem: User) => {
      return user.id === elem.id;
    });
  }

  isExcludedSecondary(user: User): boolean {
    return this.usersToExcludeSecondary.some((elem: User) => {
      return user.id === elem.id;
    });
  }

  submit(user: User): void {
    this.selectedUser.emit(user);
  }

  cancel(): void {
    this.cancelClick.emit();
  }
}
