import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {User} from "../../models/user.model";
import {Observable} from "rxjs";
import {FormControl, FormControlName} from "@angular/forms";

@Component({
  selector: 'tc-enroll-student-view',
  templateUrl: './add-user-view.component.html',
  styleUrls: ['./add-user-view.component.scss']
})
export class AddUserViewComponent {

  @Input()
  users$: Observable<User[]> = new Observable<User[]>();

  @Input()
  usersToExclude: User[] = [];

  @Input()
  actionText: string = '';

  @Input()
  excludedText: string = '';

  @Output() readonly cancelClick = new EventEmitter<void>();

  userNameFormControl = new FormControl('');

  isExcluded(user: User): boolean {
    return this.usersToExclude.some((elem: User) => {
      return user.id === elem.id;
    });
  }

  cancel(): void {
    this.cancelClick.emit();
  }
}
