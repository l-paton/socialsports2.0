import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { EventService } from '../../services/event.service';
import { Event } from './../../models/Event';

@Component({
  selector: 'app-myevents',
  templateUrl: './myevents.component.html',
  styleUrls: ['./myevents.component.css']
})
export class MyeventsComponent implements OnInit {

  eventsJoined: Event[];
  eventsApplied: Event[];
  eventsCreated: Event[];

  constructor(private userService: UserService, private eventService: EventService) { }

  ngOnInit(): void {
    this.eventService.getEventsByOrganizer().subscribe(data => this.eventsCreated = data);
    this.userService.getEventsJoined().subscribe(data => this.eventsJoined = data);
    this.userService.getEventsApplied().subscribe(data => this.eventsApplied = data);
  }

}
