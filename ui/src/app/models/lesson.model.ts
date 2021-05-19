import {Event} from "./event.model";
import {User} from "./user.model";

export enum Level {
    Beginner = 'BEGINNER',
    Intermediate = 'INTERMEDIATE',
    Advanced = 'ADVANCED'
}

export class Lesson extends Event {

    constructor(protected id: number,
                protected start: Date,
                protected end: Date,
                private capacity: number,
                private level: Level,
                private teachers: Set<User>,
                private students:Set<User>,
                ) {
        super(id, start, end);
    }
}

export type UnknownLesson = Omit<Lesson, 'id' | 'teachers' | 'students'>;