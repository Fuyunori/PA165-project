import { Event } from './event.model';
import { User } from './user.model';

export enum Level {
  Beginner = 'BEGINNER',
  Intermediate = 'INTERMEDIATE',
  Advanced = 'ADVANCED',
}

export class Lesson extends Event {
  constructor(
    public id: number,
    public start: Date,
    public end: Date,
    public capacity: number,
    public level: Level,
    public teachers: Set<User>,
    public students: Set<User>,
  ) {
    super(id, start, end);
  }
}

export type UnknownLesson = Omit<Lesson, 'id' | 'teachers' | 'students'>;
