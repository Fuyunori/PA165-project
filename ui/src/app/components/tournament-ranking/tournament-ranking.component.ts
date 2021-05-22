import {Component, Input, OnInit} from '@angular/core';
import {Ranking} from "../../models/ranking.model";

@Component({
  selector: 'tc-tournament-ranking',
  templateUrl: './tournament-ranking.component.html',
  styleUrls: ['./tournament-ranking.component.scss']
})
export class TournamentRankingComponent implements OnInit {
  @Input()
  rankings: Ranking[] = [];

  @Input()
  endDate!: Date;

  currentDate: Date = new Date();

  constructor() { }

  ngOnInit(): void {
  }

}
