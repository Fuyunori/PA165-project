import { Component, OnDestroy, OnInit } from '@angular/core';
import {async, Observable, of, Subject} from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { LessonService } from '../../../services/lesson.service';
import {filter, finalize, take, takeUntil, takeWhile} from 'rxjs/operators';
import { Lesson } from '../../../models/lesson.model';
import {User} from "../../../models/user.model";
import {UserService} from "../../../services/user.service";

@Component({
  selector: 'tc-lesson-detail',
  templateUrl: './lesson-detail.component.html',
  styleUrls: ['./lesson-detail.component.scss'],
})
export class LessonDetailComponent implements OnInit, OnDestroy {
  displayedLesson$: Observable<Lesson | null> = of(null);

  readonly userIsManager$ = this.auth.userIsManager$;

  private readonly unsubscribe$ = new Subject<void>();
  private currentlyLoggedInUserId: number | null = null;

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
    this.auth.userId$.subscribe((loggedInUserId) => {
      if(loggedInUserId != null) {
        this.userService.getUserById(loggedInUserId);
        this.currentlyLoggedInUserId = loggedInUserId;
      }
    });
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  enrollUser(displayedLesson: Lesson): void {
    if(this.currentlyLoggedInUserId) {
      this.userService.singleUser$(this.currentlyLoggedInUserId)
          .pipe(take(1), filter((user): user is User => user != null))
          .subscribe(user => {
            this.lessonService.enrollStudent(displayedLesson.id, user);
          });
    }
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
