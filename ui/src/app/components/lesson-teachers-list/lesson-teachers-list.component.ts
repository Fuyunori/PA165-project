import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../models/user.model";
import {UserService} from "../../services/user.service";
import {MatDialog} from "@angular/material/dialog";
import {AddUserViewComponent} from "../add-user-view/add-user-view.component";
import {Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";
import {Lesson} from "../../models/lesson.model";
import {LessonService} from "../../services/lesson.service";

@Component({
  selector: 'tc-lesson-teachers-list',
  templateUrl: './lesson-teachers-list.component.html',
  styleUrls: ['./lesson-teachers-list.component.scss']
})
export class LessonTeachersListComponent implements OnInit, OnDestroy {
    @Input()
    teachers: User[] = [];

    @Input()
    canAddTeacher: boolean | null = false;

    @Input()
    canRemoveTeacher: boolean | null = false;

    @Input()
    selectedLesson: {id: number} = {id: 0};

    private readonly unsubscribe$ = new Subject<void>();
    readonly users$ = this.userService.orderedUsers$;

    constructor(private readonly lessonService: LessonService,
                private readonly userService: UserService,
                private readonly dialog: MatDialog,) { }

    ngOnInit(): void {
      this.userService.getUsers();
    }

    ngOnDestroy(): void {
        this.unsubscribe$.next();
        this.unsubscribe$.complete();
    }

    addTeacher(): void {
      const dialog = this.dialog.open(AddUserViewComponent, { disableClose: true });
      dialog.componentInstance.usersToExclude = this.teachers;
      dialog.componentInstance.users$ = this.users$;
      dialog.componentInstance.actionText = "add";
      dialog.componentInstance.excludedText = "assigned already";

        dialog.componentInstance.selectedUser
            .pipe(takeUntil(this.unsubscribe$))
            .subscribe((user: User) => {
                this.lessonService.addTeacher(this.selectedLesson.id, user);
                dialog.close();
            });

        dialog.componentInstance.cancelClick
            .pipe(takeUntil(this.unsubscribe$))
            .subscribe(() => {
                dialog.close();
            });
    }
}
