import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EventService } from '../../services/event.service';
import { UserService } from '../../services/user.service';
import { TokenStorageService } from '../../services/token-storage.service';
import { Event } from '../../models/Event';


@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  events: Event[] = [];
  eventsJoined: Event[] = [];
  id: number;
  sport: string;
  startDate: Date;
  time: string;

  constructor(
    private eventService: EventService,
    private userService: UserService,
    private tokenStorageService: TokenStorageService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.getEvents();
    this.getMyEvents();
    this.id = this.tokenStorageService.getUser().user.id;
  }

  getEvents() {
    this.eventService.getListEvents().subscribe(data => {
      this.events = data;
    });
  }

  getEvent(id: number) {
    this.router.navigateByUrl('/event/' + id);
  }

  getMyEvents() {
    this.userService.getEventsJoined().subscribe(data => this.eventsJoined = data);
  }

  joinToEvent(event: Event) {
    this.eventService.joinToEvent(event.id).subscribe(() => this.ngOnInit());
  }

  leave(id: number) {
    this.eventService.leaveEvent(id).subscribe(() => this.ngOnInit());
  }

  deleteEvent(event: Event) {
    this.eventService.deleteEvent(event.id).subscribe(() => this.ngOnInit());
  }

  cancelRequest(id: number){
    this.eventService.cancelRequest(id).subscribe(() => this.ngOnInit());
  }

  isInEventsJoined(id: number): boolean {
    for (let i of this.eventsJoined) {
      if (i.id == id) {
        return true;
      }
    }

    return false;
  }

  isInEventsApplied(id: number): boolean{
    var evento = this.events.find(o => o.id == id);
    for(let j of evento.applicants){
      if(j.id == this.id){
        return true;
      }
    }
    return false;
  }

  getUserPicture(picture) {
    if (picture == null) {
      return "http://placehold.it/45x45";
    }
    return picture;
  }

  cumplesLosRequisitos(id: number){
    var evento = this.events.find(o => o.id == id);
    var age = this.calculateAge();

    if(evento.requirement.minAge > 0 && age < evento.requirement.minAge){
      return false;
    }
    if(evento.requirement.maxAge > 0 && age > evento.requirement.maxAge){
      return false;
    }
    if(evento.requirement.gender != null && evento.requirement.gender.toUpperCase() != this.tokenStorageService.getUser().user.gender.toUpperCase()){
      return false;
    }

    //comprobar reputacion

    return true;
    
  }

  calculateAge(): number{
    const today = new Date();
    const birthday = new Date(this.tokenStorageService.getUser().user.birthday);
    
    let age = today.getFullYear() - birthday.getFullYear();
    const m = today.getMonth() - birthday.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthday.getDate())) {
      age--;
    }
    return age;
  }
}
