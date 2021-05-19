import {Event} from "./event.model";
import {Ranking} from "./ranking.model";

export class Tournament extends Event {
    constructor(protected id: number,
                protected startDate: Date,
                protected endDate: Date,
                private name: string,
                private capacity: number,
                private prize: number,
                private rankings: Set<Ranking>) {
        super(id, startDate, endDate);
    }
}

export type UnknownTournament = Omit<Tournament, 'id' | 'rankings'>;