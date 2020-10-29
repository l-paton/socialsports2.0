import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EventService } from '../../services/event.service';
import { UserService } from '../../services/user.service';
import { Event } from '../../models/Event';
import { TokenStorageService } from '../../services/token-storage.service';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  events : Event[];
  eventsJoined : Event[];
  id: number;

  constructor(private eventService: EventService, private userService:UserService, private tokenStorageService: TokenStorageService, private router:Router) { }

  ngOnInit(): void {
    this.getEvents();
    this.getMyEvents();
    this.id = this.tokenStorageService.getUser().id;
  }

  getEvents(){
    this.eventService.getListEvents().subscribe(data => {
      this.events = data;
    });
  }

  joinToEvent(event:Event){
    this.eventService.joinToEvent(event.id).subscribe(() => this.ngOnInit());
  }

  deleteEvent(event:Event){
    this.eventService.deleteEvent(event.id).subscribe(() => this.ngOnInit());
  }

  getMyEvents(){
    this.userService.getEventsJoined().subscribe(data => this.eventsJoined = data);
  }

  leave(id:number){
    this.eventService.leaveEvent(id).subscribe(() => this.ngOnInit());
  }

  isInEventsJoined(id:number): boolean{
    for(let i of this.eventsJoined){
      if(i.id == id){
        return true;
      }
    }

    return false;
  }

  getUserPicture(picture){
    if(picture == null){
      return "http://placehold.it/45x45";
    }
    return picture;
  }
}
