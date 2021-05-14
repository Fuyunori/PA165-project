import { Component, EventEmitter } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Court } from 'src/app/models/court.model';

@Component({
  selector: 'tc-court-form',
  templateUrl: './court-form.component.html',
  styleUrls: ['./court-form.component.scss'],
})
export class CourtFormComponent {
  readonly courtChange = new EventEmitter<Court>();

  readonly courtForm = this.fb.group({
    name: '',
    address: '',
  });

  constructor(private readonly fb: FormBuilder) {}

  submit(): void {
    const { value } = this.courtForm;

    const court: Court = {
      name: value.name ?? '',
      address: value.address ?? '',
    };

    this.courtChange.emit(court);
  }
}
