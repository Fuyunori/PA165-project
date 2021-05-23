import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Lesson, UnknownLesson } from '../models/lesson.model';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { NotificationService } from './notification.service';
import { User } from '../models/user.model';
import {Event, UnknownEvent} from "../models/event.model";
import {EventService} from "./event.service";

const RESOURCE_URL = `${environment.apiBaseUrl}/lessons`;

type LessonsState = {
  entities: Record<number, Lesson>;
  orderedIds: number[];
};

@Injectable({
  providedIn: 'root',
})
export class LessonService {
  private readonly state$ = new BehaviorSubject<LessonsState>({
    entities: {},
    orderedIds: [],
  });

  readonly orderedLessons$: Observable<Lesson[]> = this.state$.pipe(
    map(({ entities, orderedIds }) => orderedIds.map(id => entities[id])),
  );

  readonly singleLesson$ = (id: number): Observable<Lesson | null> =>
    this.state$.pipe(map(({ entities }) => entities[id] ?? null));

  constructor(
    private readonly http: HttpClient,
    private readonly notification: NotificationService,
    private readonly eventService: EventService,
  ) {}

  getLessons(): void {
    this.http.get<Lesson[]>(RESOURCE_URL).subscribe((lessons: Lesson[]) => {
      this.state$.next({
        entities: lessons.reduce(
          (acc, lesson) => ({ ...acc, [lesson.id]: lesson }),
          {},
        ),
        orderedIds: lessons.map(({ id }) => id),
      });
    });
  }

  getLessonById(id: number): void {
    this.http
      .get<Lesson>(`${RESOURCE_URL}/${id}`)
      .subscribe((lesson: Lesson) => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [lesson.id]: lesson },
          orderedIds,
        });
      });
  }

  createLesson(lesson: UnknownLesson): void {
    this.http
      .post<Lesson>(RESOURCE_URL, lesson)
      .pipe(this.notification.onError('Could not add lesson!'))
      .subscribe((resLesson: Lesson) => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resLesson.id]: resLesson },
          orderedIds: [...orderedIds, resLesson.id],
        });
          this.eventService.getCourtEvents(resLesson.court.id);
      });
  }

  enrollStudent(lessonId: number, user: User): void {
    this.http
      .post<Lesson>(`${RESOURCE_URL}/${lessonId}/students`, user)
      .pipe(this.notification.onError('Could not enroll student!'))
      .subscribe((resLesson: Lesson) => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resLesson.id]: resLesson },
          orderedIds,
        });
          this.eventService.getCourtEvents(resLesson.court.id);
      });
  }

  addTeacher(lessonId: number, user: User): void {
    this.http
      .post<Lesson>(`${RESOURCE_URL}/${lessonId}/teachers`, user)
      .pipe(this.notification.onError('Could not assign the teacher!'))
      .subscribe((resLesson: Lesson) => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resLesson.id]: resLesson },
          orderedIds,
        });
          this.eventService.getCourtEvents(resLesson.court.id);
      });
  }

  withdrawStudent(lessonId: number, playerId: number): void {
    this.http
      .delete<Lesson>(`${RESOURCE_URL}/${lessonId}/students/${playerId}`)
        .pipe(this.notification.onError('Could not withdraw the student!'))
        .subscribe((resLesson: Lesson) => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resLesson.id]: resLesson },
          orderedIds,
        });
            this.eventService.getCourtEvents(resLesson.court.id);
      });
  }

  removeTeacher(lessonId: number, teacherId: number): void {
    this.http
      .delete<Lesson>(`${RESOURCE_URL}/${lessonId}/teachers/${teacherId}`)
        .pipe(this.notification.onError('Could not remove the teacher!'))
        .subscribe((resLesson: Lesson) => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resLesson.id]: resLesson },
          orderedIds,
        });
            this.eventService.getCourtEvents(resLesson.court.id);
      });
  }

  rescheduleLesson(lessonId: number, event: UnknownEvent): void {
      this.http
          .put<Lesson>(`${RESOURCE_URL}/${lessonId}`, event)
          .pipe(this.notification.onError('Could not reschedule the lesson!'))
          .subscribe((resLesson: Lesson) => {
              const { entities, orderedIds } = this.state$.value;
              this.state$.next({
                  entities: { ...entities, [resLesson.id]: resLesson },
                  orderedIds,
              });
              this.eventService.getCourtEvents(resLesson.court.id);
          });
  }

  deleteLesson(id: number): void {
    this.http.delete(`${RESOURCE_URL}/${id}`)
        .pipe(this.notification.onError('Could not delete the lesson!'))
        .subscribe(() => {
      const { entities, orderedIds } = this.state$.value;
      this.state$.next({
        entities: Object.values(entities)
          .filter((lesson: Lesson) => lesson.id !== id)
          .reduce(
            (acc, lesson) => ({
              ...acc,
              [lesson.id]: lesson,
            }),
            {},
          ),
        orderedIds: orderedIds.filter((orderedId: number) => orderedId !== id),
      });
      this.singleLesson$(id).subscribe((lesson) => {
          this.eventService.getCourtEvents(lesson!.court.id);
      });
    });
  }
}
