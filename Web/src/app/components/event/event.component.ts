import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import{ Event } from '../../models/Event';
import { EventService } from './../../services/event.service';
import { TokenStorageService } from './../../services/token-storage.service';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {

  idEvent: string;
  idUser: number;
  event: Event;
  requirements: string[] = [];
  editar: boolean = false;

  constructor(
    private route: ActivatedRoute, 
    private eventService: EventService, 
    private tokenStorageService: TokenStorageService,
    ) { }

  ngOnInit(): void {
    this.idUser = this.tokenStorageService.getUser().user.id;
    this.idEvent = this.route.snapshot.paramMap.get('id');
    this.getEvent();
  }

  getEvent(){
    if(this.idEvent != undefined && this.idEvent != null){
      this.eventService.getEvent(this.idEvent).subscribe(data => this.event = data);
    }
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

  isOrganizer(): boolean{
    if(this.event.organizer.id === this.idUser){
      return true;
    }
    else true;
  }

  editEvent(){
    this.editar = !this.editar;
  }

}
