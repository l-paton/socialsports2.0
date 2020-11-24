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
  time: string;
  errorMessage: string;

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

    if(this.sport && this.address){
      let e = new Event(this.sport, this.address, this.startDate, this.maxParticipants, this.price, this.comments, this.requirement, this.time);
      console.log(e);
      this.eventService.createEvent(e).subscribe(() => {
        this.router.navigateByUrl('/');
      });
    }else if(!this.sport){
      this.errorMessage = "Deporte requerido";
    }else if(!this.address){
      this.errorMessage = "Dirección requerida";
    }
    
  }

  checkCheckBoxvalue(gender: string){
    this.gender = gender;
  }

}
