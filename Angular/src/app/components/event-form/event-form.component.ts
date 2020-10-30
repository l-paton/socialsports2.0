import { Component, OnInit } from '@angular/core';
import{ EventService } from '../../services/event.service';
import { Event } from '../../models/Event'
import { Router } from '@angular/router';
import { Requirement } from 'src/app/models/Requirement';
@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.css']
})
export class EventFormComponent implements OnInit {

  requirement: Requirement;
  maxAge: number;
  minAge: number;
  gender: string;
  reputation: number;
  sport : string;
  address : string;
  startDate : string;
  maxParticipants : number;
  price : number;
  comments:string;

  constructor(private eventService : EventService, private router: Router) { 
  }

  ngOnInit(): void {
  }

  createEvent(){

    if(this.minAge == undefined){
      this.minAge = 0;
    }
    if(this.maxAge == undefined){
      this.maxAge = 0;
    }
    if(this.reputation == undefined){
      this.reputation = 0;
    }

    this.requirement = new Requirement(this.minAge, this.maxAge, this.gender, this.reputation);

    console.log(this.requirement);
    let e = new Event(this.sport, this.address, this.startDate, this.maxParticipants, this.price, this.comments, this.requirement);
    console.log(e);
    this.eventService.createEvent(e).subscribe((res:Response) => {
      this.router.navigateByUrl('/event');
    });
  }

  checkCheckBoxvalue(gender: string){
    this.gender = gender;
  }

}
