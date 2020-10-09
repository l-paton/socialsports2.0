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
    this.cargarLibros();
  }

  cargarLibros(){
    this.eventService.getListEvents().subscribe(data => {
      this.events = data;
    });
    console.log("EEEEEEEEEEEEE:" + this.events);
  }

}
