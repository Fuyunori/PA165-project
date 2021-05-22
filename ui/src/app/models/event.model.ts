import { Court } from './court.model';

export enum EventType {
  Booking = 'BOOKING',
  Tournament = 'TOURNAMENT',
  Lesson = 'LESSON',
}

export type Event = {
  type: EventType;
  id: number;
  court: Court;
  startTime: Date;
  endTime: Date;
};
