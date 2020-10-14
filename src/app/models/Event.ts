import { User } from './User';

export class Event{

    constructor(sport:string, address:string, startDate:string, maxParticipants:number, price:number, comments:string){
        this.sport = sport;
        this.address = address;
        this.startDate = startDate;
        this.maxParticipants = maxParticipants;
        this.price = price;
        this.comments = comments;
    }

    id_?: number;
    organizer: User;
    sport: string;
    address: string;
    startDate: string;
    createdAt: Date;
    maxParticipants: number;
    price: number;
    comments: string;
    finish: boolean;
    participants: User[];
}