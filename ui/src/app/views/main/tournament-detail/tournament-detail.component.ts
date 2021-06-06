import { Component, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable, of, Subject } from 'rxjs';
import {
  Tournament,
  UnknownTournament,
} from '../../../models/tournament.model';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { TournamentService } from '../../../services/tournament.service';
import { filter, take, takeUntil } from 'rxjs/operators';
import { User } from '../../../models/user.model';
import { UserService } from '../../../services/user.service';
import { Ranking } from '../../../models/ranking.model';
import { Lesson } from '../../../models/lesson.model';

@Component({
  selector: 'tc-tournament-detail',
  templateUrl: './tournament-detail.component.html',
  styleUrls: ['./tournament-detail.component.scss'],
})
export class TournamentDetailComponent implements OnInit, OnDestroy {
  displayedTournament$: Observable<Tournament | null> = of(null);
  currentlyLoggedInUser$: Observable<User | null> = of(null);
  isUserEnrolled$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
    false,
  );

  readonly userIsManager$ = this.auth.userIsManager$;
  readonly currentTime: Date = new Date();

  private readonly unsubscribe$ = new Subject<void>();

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly auth: AuthService,
    private readonly userService: UserService,
    private readonly tournamentService: TournamentService,
  ) {}

  ngOnInit(): void {
    this.route.params.pipe(takeUntil(this.unsubscribe$)).subscribe(({ id }) => {
      this.tournamentService.getTournamentById(id);
      this.displayedTournament$ = this.tournamentService.singleTournament$(id);
    });

    this.auth.userId$.subscribe(loggedInUserId => {
      if (loggedInUserId != null) {
        this.userService.getUserById(loggedInUserId);
        this.currentlyLoggedInUser$ =
          this.userService.singleUser$(loggedInUserId);
      }
    });

    combineLatest([
      this.displayedTournament$,
      this.currentlyLoggedInUser$,
    ]).subscribe(([tournament, user]: [Tournament | null, User | null]) => {
      if (tournament != null && user != null) {
        let rankings = tournament.rankings;
        if (rankings != null) {
          this.isParticipant(rankings, user);
        }
      }
    });
  }

  private isParticipant(rankings: Ranking[], user: User) {
    let result = rankings.some(ranking => {
      return ranking.player.id == user.id;
    });
    if (result) {
      this.isUserEnrolled$.next(true);
    } else {
      this.isUserEnrolled$.next(false);
    }
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  addPlayer(displayedTournament: Tournament, user: User): void {
    this.tournamentService.enrollPlayer(displayedTournament.id, user);
  }

  withdrawLoggedInPlayer(displayedTournament: Tournament, user: User): void {
    this.tournamentService.withdrawPlayer(displayedTournament.id, user.id);
  }

  withdrawPlayer(displayedTournament: Tournament, player: User): void {
    this.tournamentService.withdrawPlayer(displayedTournament.id, player.id);
  }

  convertToDate(date: Date): Date {
    return new Date(date);
  }

  rescheduleTournament(
    displayedTournament: Tournament,
    unknownTournament: UnknownTournament,
  ): void {
    this.tournamentService.rescheduleTournament(
      displayedTournament.id,
      unknownTournament,
    );
  }

  deleteTournament(displayedTournament: Tournament): void {
    if (confirm(`Permanently delete tournament ${displayedTournament.name}`)) {
      this.tournamentService.deleteTournament(displayedTournament.id);
      this.router.navigateByUrl('/main/dashboard');
    }
  }
}
