import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import{ Event } from './../../models/Event';
import { EventService } from './../../services/event.service';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {

  id: string;
  event: Event;

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

}
