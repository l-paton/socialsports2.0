import { User } from './User';
import { Requirement } from './Requirement';

export class Event{

    constructor(sport:string, address:string, startDate:string, maxParticipants:number, price:number, comments:string, requirement: Requirement, time: string){
        this.sport = sport;
        this.address = address;
        this.startDate = startDate;
        this.maxParticipants = maxParticipants;
        this.price = price;
        this.comments = comments;
        this.requirement = requirement;
        this.time = time;
    }

    id: number;
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
    applicants: User[];
    requirement: Requirement;
    time: string;
}