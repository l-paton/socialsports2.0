import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Requirement } from 'src/app/models/Requirement';
import{ Event } from '../../models/Event';
import { EventService } from './../../services/event.service';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {

  id: string;
  event: Event;
  requirements: string[] = [];

  constructor(private route: ActivatedRoute, private eventService: EventService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    this.getEvent();
  }

  getEvent(){
    if(this.id != undefined && this.id != null){
      this.eventService.getEvent(this.id).subscribe(data => this.event = data);
    }
  }

  isOrganizer(): boolean{
    return true;
  }

  getUserPicture(picture) {
    if (picture == null) {
      return "http://placehold.it/45x45";
    }
    return picture;
  }

  getRequirements(event: Event){
    for(let r in event.requirement){
      this.requirements.push(r);
    }
    return this.requirements;
  }

}
