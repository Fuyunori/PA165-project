import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'tc-rank-player-dialog',
  templateUrl: './rank-player-dialog.component.html',
  styleUrls: ['./rank-player-dialog.component.scss'],
})
export class RankPlayerDialogComponent implements OnInit {
  @Output()
  playerRank: EventEmitter<number> = new EventEmitter<number>();

  @Output() readonly cancelClick = new EventEmitter<void>();

  @Input()
  numberOfPlayers = 0;

  playerPlacement = new FormControl(1);

  ngOnInit(): void {
    this.playerPlacement.setValidators([
      Validators.min(1),
      Validators.max(this.numberOfPlayers),
    ]);
  }

  submit(): void {
    this.playerRank.emit(this.playerPlacement.value);
  }

  cancel(): void {
    this.cancelClick.emit();
  }
}
