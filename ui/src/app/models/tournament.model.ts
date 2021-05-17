import {Event} from "./event.model";

export type Tournament = Event & {
    name: string,
    capacity: number,
    prize: number,
}

export type UnknownTournament = Omit<Tournament, 'id'>;