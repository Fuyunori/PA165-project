import { Event } from './event.model';
import { Ranking } from './ranking.model';

export type Tournament = Event & {
  name: string;
  capacity: number;
  prize: number;
  rankings: Set<Ranking>;
};

export type UnknownTournament = Omit<Tournament, 'id' | 'rankings'>;
