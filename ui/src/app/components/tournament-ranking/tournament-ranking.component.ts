import {Component, EventEmitter, Input, OnDestroy, Output} from '@angular/core';
import {Ranking} from "../../models/ranking.model";
import {Observable, Subject} from "rxjs";
import {User} from "../../models/user.model";
import {MatDialog} from "@angular/material/dialog";
import {AddUserViewComponent} from "../add-user-view/add-user-view.component";
import {takeUntil} from "rxjs/operators";
import {Lesson} from "../../models/lesson.model";
import {Tournament} from "../../models/tournament.model";
import {TournamentService} from "../../services/tournament.service";

@Component({
  selector: 'tc-tournament-ranking',
  templateUrl: './tournament-ranking.component.html',
  styleUrls: ['./tournament-ranking.component.scss']
})
export class TournamentRankingComponent implements OnDestroy{
  @Output()
  withdraw: EventEmitter<User> = new EventEmitter<User>();

  @Input()
  rankings: Ranking[] = [];

  @Input()
  startDate!: Date;

  @Input()
  endDate!: Date;

  @Input()
  userIsManager$: Observable<boolean> = new Observable<boolean>();

  @Input()
  selectedTournament!: Tournament;

  private readonly unsubscribe$ = new Subject<void>();
  readonly currentDate: Date = new Date();

  constructor(private readonly tournamentService: TournamentService,
      private readonly dialog: MatDialog) {
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

  addParticipant(): void{
    let participants: User[] = this.rankings.map((ranking: Ranking) => {
      return ranking.player;
    });

    const dialog = this.dialog.open(AddUserViewComponent, { disableClose: true });
    dialog.componentInstance.usersToExcludePrimary = participants;
    dialog.componentInstance.actionText = "add participant";
    dialog.componentInstance.excludedTextPrimary = "participating already";

    dialog.componentInstance.selectedUser
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe((user: User) => {
            this.tournamentService.enrollPlayer(this.selectedTournament.id, user);
            dialog.close();
        });

    dialog.componentInstance.cancelClick
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe(() => {
          dialog.close();
        });
  }

  withdrawPlayer(player: User): void {
    this.withdraw.emit(player);
  }
}
