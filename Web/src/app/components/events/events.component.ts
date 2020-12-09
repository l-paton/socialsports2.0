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

  id: number;
  events: Event[] = [];
  eventsJoined: Event[] = [];
  
  sport: string = '';
  address: string = '';
  startDate: Date;
  time: string = '';
  score: number = 0;

  numbers: number[] = [];

  constructor(
    private eventService: EventService,
    private userService: UserService,
    private tokenStorageService: TokenStorageService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.id = this.tokenStorageService.getUser().user.id;
    this.getAllEvents();
    this.getEventsJoined();
  }

  getAllEvents() {
    this.eventService.getListEvents().subscribe(data => {
      this.events = data;
    });
  }

  getEventsJoined() {
    this.userService.getEventsJoined(this.id).subscribe(data => 
      this.eventsJoined = data);
  }

  navigateToEvent(id: number) {
    this.router.navigateByUrl('/event/' + id);
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

  buscar(){
    
    let stringDate = '';
    if(this.startDate){
      stringDate = this.startDate.toString();
    }

    if(this.sport || this.startDate || this.time || this.address || this.score > 0){
      this.events = [];
      this.eventService
        .searchEvents(this.sport, stringDate, this.time, this.address, this.score)
        .subscribe(data => 
          {
            if(data.length > 0){
              this.events = data
            }
          });
    }
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
    if(evento.requirement.gender && evento.requirement.gender.toUpperCase() != this.tokenStorageService.getUser().user.gender?.toUpperCase()){
      return false;
    }
    if(evento.requirement.reputation && evento.requirement.reputation > this.tokenStorageService.getUser().user.reputationParticipant){
      return false;
    }

    return true;
    
  }

  limpiar(){
    this.sport = '';
    this.address = '';
    this.startDate = null;
    this.time = '';
    this.score = 0;  
    (<HTMLInputElement>document.getElementById('sport')).value = '';
    (<HTMLInputElement>document.getElementById('address')).value = '';
    (<HTMLInputElement>document.getElementById('startDate')).value = '';
    (<HTMLInputElement>document.getElementById('time')).value = '';
    this.ngOnInit();
  }

  eventIsFull(event : Event): boolean{
    if(event.maxParticipants > 0 && event.maxParticipants === event.participants.length){
      return true;
    }
    else false;
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
