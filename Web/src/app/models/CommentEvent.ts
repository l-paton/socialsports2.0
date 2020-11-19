import { User } from './User';
import { Event } from './Event';

export class CommentEvent{
    id: number;
    event: Event;
    user: User;
    comment: string;
    createAt: Date;
}