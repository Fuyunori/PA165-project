import { Component, OnDestroy, OnInit } from '@angular/core';
import { async, BehaviorSubject, Observable, of, Subject } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { LessonService } from '../../../services/lesson.service';
import { filter, finalize, take, takeUntil, takeWhile } from 'rxjs/operators';
import { Lesson, UnknownLesson } from '../../../models/lesson.model';
import { User } from '../../../models/user.model';
import { UserService } from '../../../services/user.service';
import { Ranking } from '../../../models/ranking.model';

@Component({
  selector: 'tc-lesson-detail',
  templateUrl: './lesson-detail.component.html',
  styleUrls: ['./lesson-detail.component.scss'],
})
export class LessonDetailComponent implements OnInit, OnDestroy {
  displayedLesson$: Observable<Lesson | null> = of(null);
  currentlyLoggedInUser$: Observable<User | null> = of(null);
  isUserTeacher$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
    false,
  );
  isUserStudent$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
    false,
  );

  readonly userIsManager$ = this.auth.userIsManager$;

  private readonly unsubscribe$ = new Subject<void>();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly auth: AuthService,
    private readonly userService: UserService,
    private readonly lessonService: LessonService,
  ) {}

  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.unsubscribe$)).subscribe(({ id }) => {
      this.lessonService.getLessonById(id);
      this.displayedLesson$ = this.lessonService.singleLesson$(id);
    });
    this.auth.userId$.subscribe(loggedInUserId => {
      if (loggedInUserId != null) {
        this.userService.getUserById(loggedInUserId);
        this.currentlyLoggedInUser$ =
          this.userService.singleUser$(loggedInUserId);
      }
    });
    this.displayedLesson$.subscribe(lesson => {
      this.currentlyLoggedInUser$.subscribe(user => {
        if (lesson != null && user != null) {
          let teachers = lesson.teachers;
          if (teachers != null) {
            this.isTeacher(teachers, user);
          }

          let students = lesson.students;
          if (students != null) {
            this.isStudent(students, user);
          }
        }
      });
    });
  }

  private isTeacher(teachers: User[], user: User) {
    let result = teachers.some(teacher => {
      return teacher.id === user.id;
    });
    if (result) {
      this.isUserTeacher$.next(true);
    } else {
      this.isUserTeacher$.next(false);
    }
  }

  private isStudent(students: User[], user: User) {
    let result = students.some(student => {
      return student.id === user.id;
    });
    if (result) {
      this.isUserStudent$.next(true);
    } else {
      this.isUserStudent$.next(false);
    }
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  enrollUser(displayedLesson: Lesson): void {
    this.currentlyLoggedInUser$.subscribe(user => {
      if (user != null) {
        this.lessonService.enrollStudent(displayedLesson.id, user);
      }
    });
  }

  withdrawUser(displayedLesson: Lesson): void {
    this.currentlyLoggedInUser$.subscribe(user => {
      if (user != null) {
        this.lessonService.withdrawStudent(displayedLesson.id, user.id);
      }
    });
  }

  withdrawStudent(displayedLesson: Lesson, student: User): void {
    this.lessonService.withdrawStudent(displayedLesson.id, student.id);
  }

  withdrawTeacher(displayedLesson: Lesson, teacher: User): void {
    this.lessonService.removeTeacher(displayedLesson.id, teacher.id);
  }

  rescheduleLesson(lesson: Lesson, rescheduledLesson: UnknownLesson): void {
    this.lessonService.rescheduleLesson(lesson.id, rescheduledLesson);
  }

  deleteLesson(displayedLesson: Lesson): void {
    if (
      confirm(
        `Permanently delete lesson with level ${displayedLesson.level} and capacity ${displayedLesson.capacity}`,
      )
    ) {
      this.lessonService.deleteLesson(displayedLesson.id);
      this.router.navigateByUrl('/main/dashboard');
    }
  }
}
