import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Ranking} from "../../models/ranking.model";
import {Observable} from "rxjs";
import {User} from "../../models/user.model";

@Component({
  selector: 'tc-tournament-ranking',
  templateUrl: './tournament-ranking.component.html',
  styleUrls: ['./tournament-ranking.component.scss']
})
export class TournamentRankingComponent {
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

  readonly currentDate: Date = new Date();

  withdrawPlayer(player: User): void {
    this.withdraw.emit(player);
  }
}
