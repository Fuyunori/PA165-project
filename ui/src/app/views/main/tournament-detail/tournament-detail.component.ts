import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import {
  Tournament,
  UnknownTournament,
} from '../../../models/tournament.model';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { TournamentService } from '../../../services/tournament.service';
import { takeUntil } from 'rxjs/operators';
import { User } from '../../../models/user.model';

@Component({
  selector: 'tc-tournament-detail',
  templateUrl: './tournament-detail.component.html',
  styleUrls: ['./tournament-detail.component.scss'],
})
export class TournamentDetailComponent implements OnInit, OnDestroy {
  displayedTournament$: Observable<Tournament | null> = of(null);

  readonly userIsManager$ = this.auth.userIsManager$;

  private readonly unsubscribe$ = new Subject<void>();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly auth: AuthService,
    private readonly tournamentService: TournamentService,
  ) {}

  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.unsubscribe$)).subscribe(({ id }) => {
      this.tournamentService.getTournamentById(id);
      this.displayedTournament$ = this.tournamentService.singleTournament$(id);
    });
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  deleteTournament(displayedTournament: Tournament): void {
    if (confirm(`Permanently delete tournament ${displayedTournament.name}`)) {
      this.tournamentService.deleteTournament(displayedTournament.id);
      this.router.navigateByUrl('/main/dashboard');
    }
  }
}
