import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { LessonService } from '../../../services/lesson.service';
import { takeUntil } from 'rxjs/operators';
import { Lesson } from '../../../models/lesson.model';

@Component({
  selector: 'tc-lesson-detail',
  templateUrl: './lesson-detail.component.html',
  styleUrls: ['./lesson-detail.component.scss'],
})
export class LessonDetailComponent implements OnInit, OnDestroy {
  displayedLesson$: Observable<Lesson | null> = of(null);

  readonly userIsManager$ = this.auth.userIsManager$;

  private readonly unsubscribe$ = new Subject<void>();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly auth: AuthService,
    private readonly lessonService: LessonService,
  ) {}

  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.unsubscribe$)).subscribe(({ id }) => {
      this.lessonService.getLessonById(id);
      this.displayedLesson$ = this.lessonService.singleLesson$(id);
    });
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
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
