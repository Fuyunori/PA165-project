import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Level, UnknownLesson } from '../../models/lesson.model';
import { FormBuilder, Validators } from '@angular/forms';
import { EventType } from '../../models/event.model';

enum LessonFormKey {
  Start = 'Start',
  End = 'End',
  Capacity = 'Capacity',
  Level = 'Level',
}

@Component({
  selector: 'tc-lesson-form',
  templateUrl: './lesson-form.component.html',
  styleUrls: ['./lesson-form.component.scss'],
})
export class LessonFormComponent {
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

  readonly LessonFormKey = LessonFormKey;
  readonly LessonLevel = Level;

  readonly lessonForm = this.fb.group({
    [LessonFormKey.Start]: ['', Validators.required],
    [LessonFormKey.End]: ['', Validators.required],
    [LessonFormKey.Capacity]: '',
    [LessonFormKey.Level]: ['', Validators.required],
  });

  constructor(private readonly fb: FormBuilder) {}

  submit(): void {
    const { value } = this.lessonForm;

    /*const lesson: UnknownLesson = {
      type: EventType.Lesson,
      court: {},
      startTime: value[LessonFormKey.Start],
      endTime: value[LessonFormKey.End],
      capacity: value[LessonFormKey.Capacity],
      level: value[LessonFormKey.Level],
    };

    this.lessonForm.markAsPristine();
    this.lessonChange.emit(lesson);*/
  }

  cancel(): void {
    this.cancelClick.emit();
  }
}
