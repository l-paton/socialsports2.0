import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EventService } from '../../services/event.service';
import { UserService } from '../../services/user.service';
import { TokenStorageService } from '../../services/token-storage.service';
import { Event } from '../../models/Event';
import { calculateAge, eventIsFull, isApplicant, isParticipant, meetTheRequirements } from '../../_helpers/utils';

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
  
  meetTheRequirements(id: number){
    var event = this.events.find(o => o.id == id);
    var age = this.calculateAge();
    var gender = this.tokenStorageService.getUser().user.gender;
    var score = this.tokenStorageService.getUser().user.reputationParticipant;
    return meetTheRequirements(event, age, gender, score);    
  }

  eventIsFull(idEvent : number): boolean{
    var event = this.events.find(o => o.id == idEvent);
    return eventIsFull(event);
  }

  calculateAge(): number{
    return calculateAge(this.tokenStorageService.getUser().user.birthday);
  }

  isParticipant(idEvent: number): boolean {
    var event = this.events.find(o => o.id == idEvent);
    return isParticipant(event.participants, this.id);
  }

  isApplicant(idEvent: number): boolean{
    var event = this.events.find(o => o.id == idEvent);
    return isApplicant(event.applicants, this.id);
  }

  isOrganizer(idEvent: number){
    var event = this.events.find(o => o.id == idEvent);
    if (event.organizer.id === this.id) {
      return true;
    }
    else false;
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

  /*
  0. Eliminar o finalizar evento
  1. Cancelar petición
  2. Abandonar
  3. Unirse
  4. No cumples los requisitos
  */
  button(idEvent: number): number {
    
    if (this.isOrganizer(idEvent)) {
      return 0;
    }

    if (this.isApplicant(idEvent)) {
      return 1;
    }else if(this.isParticipant(idEvent)){
      return 2
    } else if(this.meetTheRequirements(idEvent) && !this.eventIsFull(idEvent)) {
      return 3;
    } else if (!this.meetTheRequirements(idEvent) && !this.eventIsFull(idEvent)) {
      return 4;
    }else{
      return 5;
    }
  }
}
