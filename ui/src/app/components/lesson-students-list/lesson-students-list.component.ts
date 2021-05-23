import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { MatDialog } from '@angular/material/dialog';
import { AddUserViewComponent } from '../add-user-view/add-user-view.component';
import { takeUntil } from 'rxjs/operators';
import { LessonService } from '../../services/lesson.service';
import { Lesson } from '../../models/lesson.model';

@Component({
  selector: 'tc-lesson-students-list',
  templateUrl: './lesson-students-list.component.html',
  styleUrls: ['./lesson-students-list.component.scss'],
})
export class LessonStudentsComponent implements OnDestroy {
  @Input()
  students: User[] = [];

  @Input()
  teachers: User[] = [];

  @Input()
  canAddStudent: boolean | null = false;

  @Input()
  canWithdrawStudent: boolean | null = false;

  @Input()
  selectedLesson!: Lesson;

  private readonly unsubscribe$ = new Subject<void>();


  constructor(private readonly lessonService: LessonService,
              private readonly dialog: MatDialog,) { }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  enrollStudent(): void {
    const dialog = this.dialog.open(AddUserViewComponent, {
      disableClose: true,
    });
    dialog.componentInstance.usersToExcludePrimary = this.students;
    dialog.componentInstance.usersToExcludeSecondary = this.teachers;
    dialog.componentInstance.actionText = "enroll";
    dialog.componentInstance.excludedTextPrimary = "enrolled already";
    dialog.componentInstance.excludedTextSecondary = "teaches lesson";

    dialog.componentInstance.selectedUser
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((user: User) => {
        this.lessonService.enrollStudent(this.selectedLesson.id, user);
        dialog.close();
      });

    dialog.componentInstance.cancelClick
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(() => {
        dialog.close();
      });
  }
}
