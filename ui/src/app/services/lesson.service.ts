import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import {Lesson, UnknownLesson} from "../models/lesson.model";
import {BehaviorSubject, Observable} from "rxjs";
import {map} from "rxjs/operators";
import {HttpClient, HttpParams} from "@angular/common/http";
import {NotificationService} from "./notification.service";
import {User} from "../models/user.model";

const RESOURCE_URL = `${environment.apiBaseUrl}/lesson`

type LessonsState = {
  entities: Record<string, Lesson>;
  orderedIds: number[]
};

@Injectable({
  providedIn: 'root'
})
export class LessonService {
  private readonly state$ = new BehaviorSubject<LessonsState>({
    entities: {},
    orderedIds: [],
  });

  readonly orderedLessons$: Observable<Lesson[]> = this.state$.pipe(
    map(({entities, orderedIds}) => orderedIds.map(id => entities[id])),
  );

  readonly singleLesson$ = (id: string): Observable<Lesson | null> =>
      this.state$.pipe(map(({entities}) => entities[id] ?? null));

  constructor(
      private readonly http: HttpClient,
      private readonly notification: NotificationService,
  ) {}

  getLessons(): void {
    this.http.get<Lesson[]>(RESOURCE_URL).subscribe((lessons: Lesson[]) => {
      this.state$.next({
        entities: lessons.reduce((acc, lesson) => ({ ...acc, [lesson.id]: lesson}), {}),
        orderedIds: lessons.map(({id}) => id),
      });
    });
  }

  getLessonById(id: number): void {
    this.http.get<Lesson>(`${RESOURCE_URL}/${id}`).subscribe((lesson: Lesson) => {
      const {entities, orderedIds} = this.state$.value;
      this.state$.next({
        entities: { ...entities, [lesson.id]: lesson},
        orderedIds,
      });
    });
  }

  createLesson(lesson: UnknownLesson): void {
    this.http
        .post<Lesson>(RESOURCE_URL, lesson)
        .pipe(this.notification.onError('Could not add lesson!'))
        .subscribe((resLesson: Lesson) => {
          const {entities, orderedIds} = this.state$.value;
          this.state$.next({
            entities: {...entities, [resLesson.id]: resLesson },
            orderedIds: [...orderedIds, resLesson.id],
          });
        });
  }

  enrollStudent(lessonId: number, user: User): void {
    this.http.post<Lesson>(`${RESOURCE_URL}/${lessonId}/students`, user)
        .subscribe((resLesson: Lesson) => {
          const { entities, orderedIds } = this.state$.value;
          this.state$.next({
            entities: {...entities, [resLesson.id]: resLesson},
            orderedIds,
          });
        });
  }

  addTeacher(lessonId: number, user: User): void {
    this.http.post<Lesson>(`${RESOURCE_URL}/${lessonId}/teachers`, user)
        .subscribe((resLesson: Lesson) => {
          const { entities, orderedIds } = this.state$.value;
          this.state$.next({
            entities: {...entities, [resLesson.id]: resLesson},
            orderedIds,
          });
        });
  }

  withdrawStudent(lessonId: number, playerId: number): void {
    this.http.delete<Lesson>(`${RESOURCE_URL}/${lessonId}/students/${playerId}`)
        .subscribe((resLesson: Lesson) => {
          const { entities, orderedIds } = this.state$.value;
          this.state$.next({
            entities: {...entities, [resLesson.id]: resLesson},
            orderedIds,
          });
        });
  }

  removeTeacher(lessonId: number, teacherId: number): void {
    this.http.delete<Lesson>(`${RESOURCE_URL}/${lessonId}/teacher/${teacherId}`)
        .subscribe((resLesson: Lesson) => {
          const { entities, orderedIds } = this.state$.value;
          this.state$.next({
            entities: {...entities, [resLesson.id]: resLesson},
            orderedIds,
          });
        });
  }

  deleteLesson(id: number): void {
    this.http.delete(`${RESOURCE_URL}/${id}`)
        .subscribe(() => {
          const { entities, orderedIds } = this.state$.value;
          this.state$.next({
            entities: Object.values(entities)
                .filter((lesson: Lesson) => lesson.id !== id)
                .reduce((acc, lesson) => ({
                  ...acc, [lesson.id]: lesson
                }), {}),
            orderedIds: orderedIds.filter((orderedId: number) => orderedId !== id),
          });
        });
  }

}