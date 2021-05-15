import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { CourtType, UnknownCourt } from 'src/app/models/court.model';

enum CourtFormKey {
  Name = 'Name',
  Address = 'Address',
  Type = 'Type',
  ImageUrl = 'ImageUrl',
}

@Component({
  selector: 'tc-court-form',
  templateUrl: './court-form.component.html',
  styleUrls: ['./court-form.component.scss'],
})
export class CourtFormComponent {
  @Output() readonly cancelClick = new EventEmitter<void>();
  @Output() readonly courtChange = new EventEmitter<UnknownCourt>();

  @Input()
  set court(court: UnknownCourt) {
    this.courtForm.setValue({
      [CourtFormKey.Name]: court.name,
      [CourtFormKey.Address]: court.address,
      [CourtFormKey.Type]: court.type,
      [CourtFormKey.ImageUrl]: court.previewImageUrl,
    });
  }

  @Input() submitButtonText = 'Submit';
  @Input() cancelButtonText = 'Cancel';

  readonly CourtFormKey = CourtFormKey;
  readonly CourtType = CourtType;

  readonly courtForm = this.fb.group({
    [CourtFormKey.Name]: ['', Validators.required],
    [CourtFormKey.Address]: ['', Validators.required],
    [CourtFormKey.Type]: ['', Validators.required],
    [CourtFormKey.ImageUrl]: '',
  });

  constructor(private readonly fb: FormBuilder) {}

  submit(): void {
    const { value } = this.courtForm;

    const court: UnknownCourt = {
      name: value[CourtFormKey.Name],
      address: value[CourtFormKey.Address],
      type: value[CourtFormKey.Type],
      previewImageUrl: value[CourtFormKey.ImageUrl],
    };

    this.courtForm.markAsPristine();
    this.courtChange.emit(court);
  }

  cancel(): void {
    this.cancelClick.emit();
  }
}
