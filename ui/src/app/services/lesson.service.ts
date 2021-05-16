import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import {Lesson, UnknownLesson} from "../models/lesson.model";
import {BehaviorSubject, Observable} from "rxjs";
import {map} from "rxjs/operators";
import {HttpClient, HttpParams} from "@angular/common/http";
import {NotificationService} from "./notification.service";

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

  enrollStudent(lessonId: number, studentId: number): void {
    let queryParams: HttpParams = new HttpParams().set('studentId', studentId.toString());
    this.http.put<Lesson>(`${RESOURCE_URL}/${lessonId}/enroll-student`, { params: queryParams} )
        .subscribe((resLesson: Lesson) => {
          const { entities, orderedIds } = this.state$.value;
          this.state$.next({
            entities: {...entities, [resLesson.id]: resLesson},
            orderedIds,
          });
        });
  }

  addTeacher(lessonId: number, teacherId: number): void {
    let queryParams: HttpParams = new HttpParams().set('teacherId', teacherId.toString());
    this.http.put<Lesson>(`${RESOURCE_URL}/${lessonId}/add-teacher`, { params: queryParams} )
        .subscribe((resLesson: Lesson) => {
          const { entities, orderedIds } = this.state$.value;
          this.state$.next({
            entities: {...entities, [resLesson.id]: resLesson},
            orderedIds,
          });
        });
  }

  withdrawStudent(lessonId: number, studentId: number): void {
    let queryParams: HttpParams = new HttpParams().set('studentId', studentId.toString());
    this.http.put<Lesson>(`${RESOURCE_URL}/${lessonId}/withdraw-student`, { params: queryParams} )
        .subscribe((resLesson: Lesson) => {
          const { entities, orderedIds } = this.state$.value;
          this.state$.next({
            entities: {...entities, [resLesson.id]: resLesson},
            orderedIds,
          });
        });
  }

  removeTeacher(lessonId: number, teacherId: number): void {
    let queryParams: HttpParams = new HttpParams().set('teacherId', teacherId.toString());
    this.http.put<Lesson>(`${RESOURCE_URL}/${lessonId}/remove-teacher`, { params: queryParams} )
        .subscribe((resLesson: Lesson) => {
          const { entities, orderedIds } = this.state$.value;
          this.state$.next({
            entities: {...entities, [resLesson.id]: resLesson},
            orderedIds,
          });
        });
  }

}