export enum EventType {
  Booking = 'BOOKING',
  Tournament = 'TOURNAMENT',
  Lesson = 'LESSON',
}

export type Event = {
  type: EventType,
  id: number;
  startTime: Date;
  endTime: Date;
};
