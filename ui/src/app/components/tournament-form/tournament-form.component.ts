import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UnknownTournament } from '../../models/tournament.model';
import { FormBuilder, Validators } from '@angular/forms';

enum TournamentFormKey {
  Start = 'Start',
  End = 'End',
  Court = 'Court',
  Name = 'Name',
  Capacity = 'Capacity',
  Prize = 'Prize',
}

@Component({
  selector: 'tc-tournament-form',
  templateUrl: './tournament-form.component.html',
  styleUrls: ['./tournament-form.component.scss'],
})
export class TournamentFormComponent {
  @Output() readonly cancelClick = new EventEmitter<void>();
  @Output() readonly tournamentChange = new EventEmitter<UnknownTournament>();

  @Input()
  set tournament(tournament: UnknownTournament) {
    this.tournamentForm.setValue({
      [TournamentFormKey.Start]: tournament.startTime,
      [TournamentFormKey.End]: tournament.endTime,
      [TournamentFormKey.Court]: tournament.court,
      [TournamentFormKey.Name]: tournament.name,
      [TournamentFormKey.Capacity]: tournament.capacity,
      [TournamentFormKey.Prize]: tournament.prize,
    });
  }

  @Input() readOnly = false;
  @Input() submitButtonText = 'Submit';
  @Input() cancelButtonText = 'Cancel';

  readonly TournamentFormKey = TournamentFormKey;

  readonly tournamentForm = this.fb.group({
    [TournamentFormKey.Start]: ['', Validators.required],
    [TournamentFormKey.End]: ['', Validators.required],
    [TournamentFormKey.Court]: [undefined, Validators.required],
    [TournamentFormKey.Name]: ['', Validators.required],
    [TournamentFormKey.Capacity]: '',
    [TournamentFormKey.Prize]: ['', Validators.required],
  });

  constructor(private readonly fb: FormBuilder) {}

  submit(): void {
    const { value } = this.tournamentForm;

    const tournament: UnknownTournament = {
      startTime: value[TournamentFormKey.Start],
      endTime: value[TournamentFormKey.End],
      court: value[TournamentFormKey.Court],
      name: value[TournamentFormKey.Name],
      capacity: value[TournamentFormKey.Capacity],
      prize: value[TournamentFormKey.Prize],
    };

    this.tournamentForm.markAsPristine();
    this.tournamentChange.emit(tournament);
  }

  cancel(): void {
    this.cancelClick.emit();
  }
}
