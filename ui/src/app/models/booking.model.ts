import { Event } from './event.model';
import { User } from './user.model';

export type Booking = Event & {
  author: User;
  users: User[];
};

export type UnknownBooking = Omit<Booking, 'id'>;
export type FormBooking = Omit<Booking, 'id' | 'author' | 'court'>;
