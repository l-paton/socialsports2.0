import { User } from './User';

export class Event{
    id_?: number;
    organizer: User;
    sport: string;
    address: string;
    startDate: Date;
    createdAt: Date;
    maxParticipants: number;
    price: number;
    comments: string;
    finish: boolean;
    participants: User[];
}