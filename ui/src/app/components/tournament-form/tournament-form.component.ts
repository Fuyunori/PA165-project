import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UnknownTournament } from '../../models/tournament.model';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { TournamentService } from '../../services/tournament.service';
import { CourtService } from '../../services/court.service';
import {filter, finalize, map, take} from 'rxjs/operators';
import { Court } from '../../models/court.model';
import { User } from '../../models/user.model';
import {BehaviorSubject, Observable, of} from 'rxjs';
import { EventType } from '../../models/event.model';
import {UnknownLesson} from "../../models/lesson.model";

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
export class TournamentFormComponent implements OnInit {
  @Output() readonly cancelClick = new EventEmitter<void>();
  @Output() readonly tournamentReschedule = new EventEmitter<UnknownTournament>();
  @Output() readonly tournamentChange = new EventEmitter<UnknownTournament>();
  @Output() readonly addUser = new EventEmitter<void>();
  @Output() readonly withdrawUser = new EventEmitter<void>();

  @Input()
  set tournament(tournament: UnknownTournament) {
    this.tournamentForm.setValue({
      [TournamentFormKey.Start]: tournament.startTime,
      [TournamentFormKey.End]: tournament.endTime,
      [TournamentFormKey.Court]: tournament.court.id,
      [TournamentFormKey.Name]: tournament.name,
      [TournamentFormKey.Capacity]: tournament.capacity,
      [TournamentFormKey.Prize]: tournament.prize,
    });
  }

  @Input() readOnly = false;
  @Input() adding = false;
  @Input() reschedule = false;
  @Input() court$ = new Observable<Court | null>();
  @Input() isEnrolledAlready$ = new BehaviorSubject<boolean>(false);
  @Input() submitButtonText = 'Submit';
  @Input() cancelButtonText = 'Cancel';

  readonly courts$ = this.courtService.orderedCourts$;

  readonly TournamentFormKey = TournamentFormKey;
  readonly currentTime = new Date();
  hasStarted: boolean = false;
  hasEnded: boolean = false;

  readonly tournamentForm = this.fb.group({
    [TournamentFormKey.Start]: ['', Validators.required],
    [TournamentFormKey.End]: ['', Validators.required],
    [TournamentFormKey.Court]: [undefined, Validators.required],
    [TournamentFormKey.Name]: ['', Validators.required],
    [TournamentFormKey.Capacity]: '',
    [TournamentFormKey.Prize]: ['', Validators.required],
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly tournamentService: TournamentService,
    private readonly courtService: CourtService,
  ) {}

  ngOnInit(): void {
    this.courtService.getCourts();
    this.court$.subscribe(court => {
      if (court != null) {
        this.tournamentForm.get(TournamentFormKey.Court)?.setValue(court.id);
      }
    });
    this.tournamentForm.controls[TournamentFormKey.Start].setValidators([
      this.isLessThanCurrentTimeValidation,
    ]);

    this.tournamentForm.controls[TournamentFormKey.End].setValidators([
      this.isLessThanCurrentTimeValidation,
    ]);
    this.tournamentForm.setValidators(this.dateValidation);
    this.computeHasStarted();
    this.computeHasEnded();
  }

  computeHasStarted(): boolean {
    let currentTime: Date = new Date();
    let startDate: Date = new Date(
      this.tournamentForm.get(TournamentFormKey.Start)?.value,
    );
    return currentTime > startDate;
  }

  computeHasEnded(): boolean {
    let currentTime: Date = new Date();
    let endDate: Date = new Date(
      this.tournamentForm.get(TournamentFormKey.End)?.value,
    );
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
    let startDate = new Date(formGroup.controls[TournamentFormKey.Start].value);
    let endDate = new Date(formGroup.controls[TournamentFormKey.End].value);

    if (startDate > endDate) {
      formGroup.controls[TournamentFormKey.Start].setErrors({error: 'Start date must be before the end date.' });
      formGroup.controls[TournamentFormKey.End].setErrors({error: 'Start date must be before the end date.' });
      return { error: 'Start date must be before the end date.' };
    } else {
      formGroup.controls[TournamentFormKey.Start].setErrors(null);
      formGroup.controls[TournamentFormKey.End].setErrors(null);
    }
    return null;
  };

  submit(): void {
    const { value } = this.tournamentForm;

    this.courtService
      .singleCourt$(value[TournamentFormKey.Court])
      .pipe(
        take(1),
        filter((court): court is Court => court != null),
      )
      .subscribe(court => {
        const tournament: UnknownTournament = {
          type: EventType.Tournament,
          startTime: value[TournamentFormKey.Start],
          endTime: value[TournamentFormKey.End],
          court,
          name: value[TournamentFormKey.Name],
          capacity: value[TournamentFormKey.Capacity],
          prize: value[TournamentFormKey.Prize],
        };

        this.tournamentForm.markAsPristine();
        if(!this.reschedule.valueOf()) {
          this.tournamentChange.emit(tournament);
        } else {
          this.rescheduleTournament(tournament);
        }
      });
  }

  rescheduleTournament(tournament: UnknownTournament): void {
    this.tournamentReschedule.emit(tournament);
  }

  addPlayer(): void {
    this.addUser.emit();
  }

  withdrawPlayer(): void {
    this.withdrawUser.emit();
  }

  cancel(): void {
    this.cancelClick.emit();
  }
}
