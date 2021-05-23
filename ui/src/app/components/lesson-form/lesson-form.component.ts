import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { Level, UnknownLesson } from '../../models/lesson.model';
import {AbstractControl, FormBuilder, Validators} from '@angular/forms';
import { EventType } from '../../models/event.model';
import { CourtService } from '../../services/court.service';
import {filter, map, take} from 'rxjs/operators';
import { Court } from '../../models/court.model';
import { LessonService } from '../../services/lesson.service';
import { AuthService } from '../../services/auth.service';
import { BehaviorSubject, Observable } from 'rxjs';

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
  @Output() readonly enrollUser = new EventEmitter<void>();
  @Output() readonly withdrawUser = new EventEmitter<void>();

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

  readonly courts$ = this.courtService.orderedCourts$;

  readonly LessonFormKey = LessonFormKey;
  readonly LessonLevel = Level;
  readonly currentTime = new Date();

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
  ) {}

  ngOnInit(): void {
    this.courtService.getCourts();
    this.court$.subscribe(court => {
      if (court != null) {
        this.lessonForm.get(LessonFormKey.Court)?.setValue(court.id);
      }
    });
    this.lessonForm.controls[LessonFormKey.Start].setValidators([this.isLessThanCurrentTimeValidation, this.isGreaterThanEndTimeValidation]);
    this.lessonForm.controls[LessonFormKey.End].setValidators([this.isLessThanCurrentTimeValidation, this.isSmallerThanStartTimeValidation]);
  }
  hasStarted(): boolean {
    let currentTime: Date = new Date();
    let startDate: Date = new Date(this.lessonForm.get(LessonFormKey.Start)?.value);
    return currentTime > startDate;
  }

  hasEnded(): boolean {
    let currentTime: Date = new Date();
    let endDate: Date = new Date(this.lessonForm.get(LessonFormKey.End)?.value);
    return currentTime > endDate;
  }

  isLessThanCurrentTimeValidation = (form: AbstractControl) => {
    let formDate = new Date(form.value);

    if(formDate < this.currentTime){
      return { error: "The date must at least in the present time."};
    }
    return null;
  }

  isGreaterThanEndTimeValidation = (form: AbstractControl) => {
    let formDate = new Date(form.value);
    let endDate = new Date(this.lessonForm.controls[LessonFormKey.End].value);

    if(formDate > endDate){
      return { error: "Start date must be before the end date."};
    }
    return null;
  }

  isSmallerThanStartTimeValidation = (form: AbstractControl) => {
    let formDate = new Date(form.value);
    let startDate = new Date(this.lessonForm.controls[LessonFormKey.Start].value);

    if(formDate < startDate){
      return { error: "End date must be after the start date."};
    }
    return null;
  }

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
        this.lessonChange.emit(lesson);
      });
  }

  enroll(): void {
    this.enrollUser.emit();
  }

  withdraw(): void {
    this.withdrawUser.emit();
  }

  cancel(): void {
    this.cancelClick.emit();
  }
}
