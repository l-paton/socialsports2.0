import { Component, OnInit } from '@angular/core';
import { EventService } from '../../services/event.service';
import { Event } from '../../models/Event';
import { TokenStorageService } from '../../services/token-storage.service';
import { EmailValidator } from '@angular/forms';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  events : Event[];
  id: number;

  constructor(private eventService: EventService, private tokenStorageService: TokenStorageService) { }

  ngOnInit(): void {
    this.getEvents();
    this.id = this.tokenStorageService.getUser().id;
  }

  getEvents(){
    this.eventService.getListEvents().subscribe(data => {
      this.events = data;
    });
  }

  joinToEvent(event:Event){
    this.eventService.joinToEvent(event.id).subscribe(() => window.location.reload());
  }

  deleteEvent(event:Event){
    this.eventService.deleteEvent(event.id).subscribe(() => window.location.reload());
  }

}
