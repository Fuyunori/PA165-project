import { environment } from '../../environments/environment';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Tournament, UnknownTournament } from '../models/tournament.model';
import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { NotificationService } from './notification.service';
import { Ranking } from '../models/ranking.model';
import { User } from '../models/user.model';
import {UnknownEvent} from "../models/event.model";
import {EventService} from "./event.service";

const RESOURCE_URL = `${environment.apiBaseUrl}/tournaments`;

type TournamentState = {
  entities: Record<number, Tournament>;
  orderedIds: number[];
};

@Injectable({ providedIn: 'root' })
export class TournamentService {
  private readonly state$ = new BehaviorSubject<TournamentState>({
    entities: {},
    orderedIds: [],
  });

  readonly singleTournament$ = (id: number): Observable<Tournament | null> =>
    this.state$.pipe(map(({ entities }) => entities[id] ?? null));

  constructor(
    private readonly http: HttpClient,
    private readonly notification: NotificationService,
    private readonly eventService: EventService,
  ) {}

  getTournaments(): void {
    this.http
      .get<Tournament[]>(RESOURCE_URL)
      .subscribe((tournaments: Tournament[]) => {
        this.state$.next({
          entities: tournaments.reduce(
            (acc, tournament) => ({ ...acc, [tournament.id]: tournament }),
            {},
          ),
          orderedIds: tournaments.map(({ id }) => id),
        });
      });
  }

  getTournamentById(id: number): void {
    this.http
      .get<Tournament>(`${RESOURCE_URL}/${id}`)
      .subscribe((tournament: Tournament) => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [tournament.id]: tournament },
          orderedIds,
        });
      });
  }

  createTournament(tournament: UnknownTournament): void {
    this.http
      .post<Tournament>(RESOURCE_URL, tournament)
      .pipe(this.notification.onError('Could not create a tournament!'))
      .subscribe((resTournament: Tournament) => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resTournament.id]: resTournament },
          orderedIds: [...orderedIds, resTournament.id],
        });
          this.eventService.getCourtEvents(resTournament.court.id);
      });
  }

  enrollPlayer(tournamentId: number, user: User): void {
    this.http
      .post<Tournament>(`${RESOURCE_URL}/${tournamentId}/players`, user)
        .pipe(this.notification.onError('Could not enroll the player!'))
        .subscribe((resTournament: Tournament) => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resTournament.id]: resTournament },
          orderedIds,
        });
            this.eventService.getCourtEvents(resTournament.court.id);
      });
  }

  withdrawPlayer(tournamentId: number, playerId: number): void {
    this.http
      .delete<Tournament>(`${RESOURCE_URL}/${tournamentId}/players/${playerId}`)
        .pipe(this.notification.onError('Could not withdraw the player!'))
        .subscribe((resTournament: Tournament) => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resTournament.id]: resTournament },
          orderedIds,
        });
            this.eventService.getCourtEvents(resTournament.court.id);
      });
  }

  rankPlayer(tournamentId: number, ranking: Ranking): void {
    this.http
      .put<Tournament>(`${RESOURCE_URL}/${tournamentId}/rankings`, ranking)
      .subscribe((resTournament: Tournament) => {
        const { entities, orderedIds } = this.state$.value;
        this.state$.next({
          entities: { ...entities, [resTournament.id]: resTournament },
          orderedIds,
        });
          this.eventService.getCourtEvents(resTournament.court.id);
      });
  }

  rescheduleTournament(tournamentId: number, event: UnknownEvent): void {
    this.http
        .put<Tournament>(`${RESOURCE_URL}/${tournamentId}`, event)
        .pipe(this.notification.onError('Could not reschedule the tournament!'))
        .subscribe((resTournament: Tournament) => {
            const { entities, orderedIds } = this.state$.value;
            this.state$.next({
                entities: { ...entities, [resTournament.id]: resTournament },
                orderedIds,
            });
            this.eventService.getCourtEvents(resTournament.court.id);
        });
  }

  deleteTournament(id: number): void {
    this.http.delete(`${RESOURCE_URL}/${id}`)
        .pipe(this.notification.onError('Could not delete the tournament!'))
        .subscribe(() => {
      const { entities, orderedIds } = this.state$.value;
      this.state$.next({
        entities: Object.values(entities)
          .filter((tournament: Tournament) => tournament.id !== id)
          .reduce(
            (acc, tournament: Tournament) => ({
              ...acc,
              [tournament.id]: tournament,
            }),
            {},
          ),
        orderedIds: orderedIds.filter(orderedId => orderedId !== id),
      });
            this.singleTournament$(id).subscribe((tournament) => {
                this.eventService.getCourtEvents(tournament!.court.id);
            });
    });
  }
}
