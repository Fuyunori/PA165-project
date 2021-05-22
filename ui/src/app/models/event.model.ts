import { Court } from './court.model';

export type Event = {
  id: number;
  startTime: Date;
  endTime: Date;
  court: Court;
};
