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

  isInEventsJoined(id: number): boolean {
    for (let i of this.eventsJoined) {
      if (i.id == id) {
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
}
