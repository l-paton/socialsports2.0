import { Component, OnInit } from '@angular/core';
import { EventService } from '../../services/event.service';
import { Event } from '../../models/Event';


@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  events : Event[];

  constructor(private eventService: EventService) { }

  ngOnInit(): void {
    this.getEvents();
  }

  getEvents(){
    this.eventService.getListEvents().subscribe(data => {
      this.events = data;
    });
  }

  joinToEvent(event:Event){
    console.log(event.id);
    this.eventService.joinToEvent(event.id).subscribe(Response);
    window.location.reload();
  }

}
