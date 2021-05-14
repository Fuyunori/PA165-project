import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { UnknownCourt } from 'src/app/models/court.model';

@Component({
  selector: 'tc-court-form',
  templateUrl: './court-form.component.html',
  styleUrls: ['./court-form.component.scss'],
})
export class CourtFormComponent {
  @Output() readonly courtChange = new EventEmitter<UnknownCourt>();

  @Input()
  set court(court: UnknownCourt) {
    this.courtForm.setValue({
      name: court.name,
      address: court.address,
    });
  }

  readonly courtForm = this.fb.group({
    name: '',
    address: '',
  });

  constructor(private readonly fb: FormBuilder) {}

  submit(): void {
    const { value } = this.courtForm;

    const court: UnknownCourt = {
      name: value.name ?? '',
      address: value.address ?? '',
    };

    this.courtChange.emit(court);
  }
}
