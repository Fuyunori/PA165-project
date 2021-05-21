import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { Level, UnknownLesson } from '../../models/lesson.model';
import { FormBuilder, Validators } from '@angular/forms';
import {CourtService} from "../../services/court.service";

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

  @Input()
  set lesson(lesson: UnknownLesson) {
    this.lessonForm.setValue({
      [LessonFormKey.Start]: lesson.startTime,
      [LessonFormKey.End]: lesson.endTime,
      [LessonFormKey.Capacity]: lesson.capacity,
      [LessonFormKey.Level]: lesson.level,
    });
  }

  @Input() readOnly = false;
  @Input() submitButtonText = 'Submit';
  @Input() cancelButtonText = 'Cancel';

  readonly courts$ = this.courtService.orderedCourts$;

  readonly LessonFormKey = LessonFormKey;
  readonly LessonLevel = Level;
  readonly currentTime = new Date();

  readonly lessonForm = this.fb.group({
    [LessonFormKey.Start]: ['', Validators.required],
    [LessonFormKey.End]: ['', Validators.required],
    [LessonFormKey.Court]: [undefined, Validators.required],
    [LessonFormKey.Capacity]: '',
    [LessonFormKey.Level]: ['', Validators.required],
  });

  constructor(private readonly fb: FormBuilder,
              private readonly courtService: CourtService) {}

  ngOnInit(): void {
    this.courtService.getCourts();
  }

  submit(): void {
    const { value } = this.lessonForm;

    const lesson: UnknownLesson = {
      startTime: value[LessonFormKey.Start],
      endTime: value[LessonFormKey.End],
      court: value[LessonFormKey.Court],
      capacity: value[LessonFormKey.Capacity],
      level: value[LessonFormKey.Level],
    };

    this.lessonForm.markAsPristine();
    this.lessonChange.emit(lesson);
  }

  cancel(): void {
    this.cancelClick.emit();
  }
}
