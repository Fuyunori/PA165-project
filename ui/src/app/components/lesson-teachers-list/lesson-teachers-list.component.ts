import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { MatDialog } from '@angular/material/dialog';
import { AddUserViewComponent } from '../add-user-view/add-user-view.component';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Lesson } from '../../models/lesson.model';
import { LessonService } from '../../services/lesson.service';

@Component({
  selector: 'tc-lesson-teachers-list',
  templateUrl: './lesson-teachers-list.component.html',
  styleUrls: ['./lesson-teachers-list.component.scss'],
})

export class LessonTeachersListComponent implements OnDestroy {
  @Input()
  students: User[] = [];

  @Input()
  teachers: User[] = [];

  @Input()
  canAddTeacher: boolean | null = false;

  @Input()
  canRemoveTeacher: boolean | null = false;

  @Input()
  selectedLesson!: Lesson;

  private readonly unsubscribe$ = new Subject<void>();

  constructor(private readonly lessonService: LessonService,
                private readonly dialog: MatDialog,) { }



  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

    addTeacher(): void {
      const dialog = this.dialog.open(AddUserViewComponent, { disableClose: true });
      dialog.componentInstance.usersToExcludePrimary = this.teachers;
      dialog.componentInstance.usersToExcludeSecondary = this.students;
      dialog.componentInstance.actionText = "add";
      dialog.componentInstance.excludedTextPrimary = "assigned already";
      dialog.componentInstance.excludedTextSecondary = "enrolled already";

    dialog.componentInstance.selectedUser
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((user: User) => {
        if (this.selectedLesson != null) {
          this.lessonService.addTeacher(this.selectedLesson.id, user);
          dialog.close();
        }
      });

    dialog.componentInstance.cancelClick
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(() => {
        dialog.close();
      });
  }
}
