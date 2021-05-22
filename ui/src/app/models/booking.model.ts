import { Event } from './event.model';
import { User } from './user.model';

export type Booking = Event & {
    author: User;
    users: User[];
};

export type UnknownBooking = Omit<Event, "id">;