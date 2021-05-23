import { Pipe, PipeTransform } from '@angular/core';
import { Ranking } from '../models/ranking.model';

@Pipe({
  name: 'sortRankings',
})
export class SortRankingsPipe implements PipeTransform {
  transform(rankings: Ranking[]): Ranking[] {
    return rankings.sort((a: Ranking, b: Ranking) => {
      return a.playerPlacement - b.playerPlacement;
    });
  }
}
