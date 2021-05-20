import {Event} from "./event.model";
import {Ranking} from "./ranking.model";

export class Tournament extends Event {
    constructor(public id: number,
                public startDate: Date,
                public endDate: Date,
                public name: string,
                public capacity: number,
                public prize: number,
                public rankings: Set<Ranking>) {
        super(id, startDate, endDate);
    }
}

export type UnknownTournament = Omit<Tournament, 'id' | 'rankings'>;