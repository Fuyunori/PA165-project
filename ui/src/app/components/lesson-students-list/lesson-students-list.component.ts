import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Observable, of, Subject} from "rxjs";
import {User} from "../../models/user.model";
import {UserService} from "../../services/user.service";
import {MatDialog} from "@angular/material/dialog";
import {AddUserViewComponent} from "../add-user-view/add-user-view.component";
import {takeUntil} from "rxjs/operators";

@Component({
  selector: 'tc-lesson-students-list',
  templateUrl: './lesson-students-list.component.html',
  styleUrls: ['./lesson-students-list.component.scss']
})
export class LessonStudentsComponent implements OnInit, OnDestroy {
  @Input()
  students: User[] = [];

  @Input()
  canAddStudent: boolean | null = false;

  @Input()
  canWithdrawStudent: boolean | null = false;

  private readonly unsubscribe$ = new Subject<void>();
  readonly users$ = this.userService.orderedUsers$;

  constructor(private readonly userService: UserService,
              private readonly dialog: MatDialog,) { }

  ngOnInit(): void {
    this.userService.getUsers();
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  enrollStudent(): void {
    const dialog = this.dialog.open(AddUserViewComponent, { disableClose: true });
    dialog.componentInstance.usersToExclude = this.students;
    dialog.componentInstance.users$ = this.users$;
    dialog.componentInstance.actionText = "enroll";
    dialog.componentInstance.excludedText = "enrolled already";

    dialog.componentInstance.cancelClick
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe(() => {
          dialog.close();
        });
  }
}
