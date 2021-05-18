import {Component, EventEmitter, Input, Output} from '@angular/core';
import { Level, UnknownLesson} from "../../models/lesson.model";
import {FormBuilder, Validators} from "@angular/forms";

enum LessonFormKey {
  Capacity = 'Capacity',
  Level = 'Level',
}

@Component({
  selector: 'tc-lesson-form',
  templateUrl: './lesson-form.component.html',
  styleUrls: ['./lesson-form.component.scss']
})
export class LessonFormComponent {
  @Output() readonly cancelClick = new EventEmitter<void>();
  @Output() readonly lessonChange = new EventEmitter<UnknownLesson>();

  @Input()
  set lesson(lesson: UnknownLesson){
    this.lessonForm.setValue({
      [LessonFormKey.Capacity]: lesson.capacity,
      [LessonFormKey.Level]: lesson.level,
    });
  }

  @Input() readonly = false;
  @Input() submitButtonText = 'Submit';
  @Input() cancelButtonText = 'Cancel';

  readonly LessonFormKey = LessonFormKey;
  readonly LessonLevel = Level;

  readonly lessonForm = this.fb.group({
    [LessonFormKey.Capacity]: '',
    [LessonFormKey.Level]: ['', Validators.required]
  });

  constructor(private readonly fb: FormBuilder) { }

  submit(): void {
    const { value } = this.lessonForm;

    const lesson: UnknownLesson = {
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
