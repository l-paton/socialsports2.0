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

  editStartDate: string;
  editTime: string;
  editMaxParticipants: number;
  editPrice: number;
  editComments: string;
  editMinAge: number;
  editMaxAge: number;
  editGender: string;

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
    else false;
  }

  editEvent(){
    this.editar = !this.editar;
  }

  acceptChanges(){

    if(this.editStartDate){
      this.eventService.editStartDate(this.event.id, this.editStartDate).subscribe();
    }
    if(this.editTime){
      this.eventService.editTime(this.event.id, this.editTime).subscribe();
    }
    if(this.editMaxParticipants){
      this.eventService.editMaxParticipants(this.event.id, this.editMaxParticipants).subscribe();
    }
    
  }

  cumplesLosRequisitos(){
    var age = this.calculateAge();

    if(this.event.requirement.minAge > 0 && age < this.event.requirement.minAge){
      console.log("min age");
      return false;
    }
    if(this.event.requirement.maxAge > 0 && age > this.event.requirement.maxAge){
      console.log("max age");
      return false;
    }
    if(this.event.requirement.gender != null && this.event.requirement.gender.toUpperCase() != this.tokenStorageService.getUser().user.gender.toUpperCase()){
      console.log(this.tokenStorageService.getUser().user.gender.toUpperCase());
      console.log("gender");
      return false;
    }
    //comprobar reputacion
    
    return true;
    
  }

  calculateAge(): number{
    const today = new Date();
    const birthday = new Date(this.tokenStorageService.getUser().user.birthday);
    
    let age = today.getFullYear() - birthday.getFullYear();
    const m = today.getMonth() - birthday.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthday.getDate())) {
      age--;
    }
    return age;
  }
}
