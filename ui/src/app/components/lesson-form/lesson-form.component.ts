import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Level, UnknownLesson } from '../../models/lesson.model';
import {AbstractControl, Form, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from '@angular/forms';
import { EventType } from '../../models/event.model';
import { CourtService } from '../../services/court.service';
import {filter, finalize, map, take} from 'rxjs/operators';
import { Court } from '../../models/court.model';
import { LessonService } from '../../services/lesson.service';
import { AuthService } from '../../services/auth.service';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {UserService} from "../../services/user.service";
import {User} from "../../models/user.model";

enum LessonFormKey {
  Start = 'Start',
  End = 'End',
  Court = 'Court',
  Capacity = 'Capacity',
  Level = 'Level',
}

@Component({
  selector: 'tc-lesson-form',
  templateUrl: './lesson-form.component.html',
  styleUrls: ['./lesson-form.component.scss'],
})
export class LessonFormComponent implements OnInit {
  @Output() readonly cancelClick = new EventEmitter<void>();
  @Output() readonly lessonChange = new EventEmitter<UnknownLesson>();
  @Output() readonly lessonReschedule = new EventEmitter<UnknownLesson>();
  @Output() readonly enrollUser = new EventEmitter<User>();
  @Output() readonly withdrawUser = new EventEmitter<User>();

  @Input()
  set lesson(lesson: UnknownLesson) {
    this.lessonForm.setValue({
      [LessonFormKey.Start]: lesson.startTime,
      [LessonFormKey.End]: lesson.endTime,
      [LessonFormKey.Court]: lesson.court.id,
      [LessonFormKey.Capacity]: lesson.capacity,
      [LessonFormKey.Level]: lesson.level,
    });
  }

  @Input() readOnly = false;
  @Input() adding = false;
  @Input() reschedule = false;
  @Input() court$ = new Observable<Court | null>();
  @Input() isStudent$ = new BehaviorSubject<boolean>(false);
  @Input() isTeacher$ = new BehaviorSubject<boolean>(false);
  @Input() submitButtonText = 'Submit';
  @Input() cancelButtonText = 'Cancel';
  @Input() currentlyLoggedInUser$: Observable<User | null> = new Observable<User | null>();

  readonly courts$ = this.courtService.orderedCourts$;
  readonly LessonFormKey = LessonFormKey;
  readonly LessonLevel = Level;
  readonly currentTime = new Date();
  hasStarted: boolean = false;
  hasEnded: boolean = false;

  readonly lessonForm = this.fb.group({
    [LessonFormKey.Start]: ['', Validators.required],
    [LessonFormKey.End]: ['', Validators.required],
    [LessonFormKey.Court]: [null, Validators.required],
    [LessonFormKey.Capacity]: '',
    [LessonFormKey.Level]: ['', Validators.required],
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly lessonService: LessonService,
    private readonly courtService: CourtService,
    private readonly userService: UserService,
  ) {}

  ngOnInit(): void {
    this.courtService.getCourts();
    this.court$.subscribe(court => {
      if (court != null) {
        this.lessonForm.get(LessonFormKey.Court)?.setValue(court.id);
      }
    });
    this.lessonForm.controls[LessonFormKey.Start].setValidators([
      this.isLessThanCurrentTimeValidation,
    ]);
    this.lessonForm.controls[LessonFormKey.End].setValidators([
      this.isLessThanCurrentTimeValidation,
    ]);
    this.lessonForm.setValidators(this.dateValidation);
    this.computeHasStarted();
    this.computeHasEnded();

    this.authService.userId$.subscribe(loggedInUserId => {
      if (loggedInUserId != null) {
        this.userService.getUserById(loggedInUserId);
        this.currentlyLoggedInUser$ = this.userService.singleUser$(loggedInUserId);
      }
    });
  }

  computeHasStarted(): boolean {
    let currentTime: Date = new Date();
    let startDate: Date = new Date(
      this.lessonForm.get(LessonFormKey.Start)?.value,
    );
    return currentTime > startDate;
  }

  computeHasEnded(): boolean {
    let currentTime: Date = new Date();
    let endDate: Date = new Date(this.lessonForm.get(LessonFormKey.End)?.value);
    return currentTime > endDate;
  }

  isReadOnly(): boolean {
    return this.readOnly || this.hasEnded;
  }

  isLessThanCurrentTimeValidation = (form: AbstractControl) => {
    let formDate = new Date(form.value);

    if (formDate < this.currentTime) {
      return { error: 'The date must at least in the present time.' };
    }
    return null;
  };

  dateValidation: ValidatorFn = (form: AbstractControl): ValidationErrors | null => {
    let formGroup: FormGroup = form as FormGroup;
    let startDate = new Date(formGroup.controls[LessonFormKey.Start].value);
    let endDate = new Date(formGroup.controls[LessonFormKey.End].value);

    if (startDate > endDate) {
      formGroup.controls[LessonFormKey.Start].setErrors({error: 'Start date must be before the end date.' });
      formGroup.controls[LessonFormKey.End].setErrors({error: 'Start date must be before the end date.' });
      return { error: 'Start date must be before the end date.' };
    } else {
      formGroup.controls[LessonFormKey.Start].setErrors(null);
      formGroup.controls[LessonFormKey.End].setErrors(null);
    }
    return null;
  };

  submit(): void {
    const { value } = this.lessonForm;

    this.courtService
      .singleCourt$(value[LessonFormKey.Court])
      .pipe(
        take(1),
        filter((court): court is Court => court != null),
      )
      .subscribe(court => {
        const lesson: UnknownLesson = {
          type: EventType.Lesson,
          startTime: value[LessonFormKey.Start],
          endTime: value[LessonFormKey.End],
          court,
          capacity: value[LessonFormKey.Capacity],
          level: value[LessonFormKey.Level],
        };

        this.lessonForm.markAsPristine();
        if(!this.reschedule.valueOf()){
          this.lessonChange.emit(lesson);
        } else {
          this.rescheduleLesson(lesson);
        }
      });
  }

  rescheduleLesson(lesson: UnknownLesson): void {
    this.lessonReschedule.emit(lesson);
  }

  enroll(): void {
    this.currentlyLoggedInUser$.subscribe(user => {
      if(user){
        this.enrollUser.emit(user);
      }
    });
  }

  withdraw(): void {
    this.currentlyLoggedInUser$.subscribe(user => {
      if(user){
        this.withdrawUser.emit(user);
      }
    });
  }

  cancel(): void {
    this.cancelClick.emit();
  }
}
