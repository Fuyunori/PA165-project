import {Event} from "./event.model";
import {User} from "./user.model";

export enum Level {
    Beginner = 'BEGINNER',
    Intermediate = 'INTERMEDIATE',
    Advanced = 'ADVANCED'
}

export type Lesson = Event & {
    capacity: number;
    level: Level;
    teachers: Set<User>;
    students: Set<User>;
}

export type UnknownLesson = Omit<Lesson, 'id' | 'teachers' | 'students'>;