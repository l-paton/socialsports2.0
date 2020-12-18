import { Component, Inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Event } from '../../models/Event';
import { TokenStorageService } from './../../services/token-storage.service';
import { EventService } from './../../services/event.service';
import { RateService } from './../../services/rate.service';

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
  scoreOrganizer: number = 0;
  editAddress: string;
  editStartDate: string;
  editTime: string;
  editMaxParticipants: number;
  editPrice: number;
  editComments: string;
  editMinAge: number;
  editMaxAge: number;
  editGender: string = null;
  commentEvent: string;
  errorMessage: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tokenStorageService: TokenStorageService,
    private eventService: EventService,
    private rateService: RateService
  ) { }

  ngOnInit(): void {
    this.idUser = this.tokenStorageService.getUser().user.id;
    this.idEvent = this.route.snapshot.paramMap.get('id');
    this.getEvent();
  }

  getEvent() {
    if (this.idEvent != undefined && this.idEvent != null) {
      this.eventService.getEvent(this.idEvent).subscribe(data => {
        this.event = data;
        this.getEventComments();
      });
    }
  }

  async acceptChanges() {

    if(this.editAddress){
      this.eventService.editAddress(this.event.id, this.editAddress).subscribe(() => {
        this.event.address = this.editAddress;
      })
    }

    await this.sleep(250);

    if (this.editStartDate) {
      this.eventService.editStartDate(this.event.id, this.editStartDate).subscribe(() => {
        this.event.startDate = this.editStartDate;
      });
    }

    await this.sleep(250);

    if (this.editTime) {
      this.eventService.editTime(this.event.id, this.editTime).subscribe(() => {
        this.event.time = this.editTime;
      });
    }

    await this.sleep(250);

    if (this.editMaxParticipants) {
      this.eventService.editMaxParticipants(this.event.id, this.editMaxParticipants).subscribe(() => {
        this.event.maxParticipants = this.editMaxParticipants;
      });
    }

    await this.sleep(250);

    if(this.editComments){
      this.eventService.editComment(this.event.id, this.editComments).subscribe(() => {
        this.event.comments = this.editComments;
      });
    }

    await this.sleep(250);

    if(this.editMinAge){
      this.eventService.editMinAge(this.event.id, this.editMinAge).subscribe(() => {
        this.event.requirement.minAge = this.editMinAge;
      });
    }

    await this.sleep(250);

    if(this.editMaxAge){
      this.eventService.editMaxAge(this.event.id, this.editMaxAge).subscribe(() => {
        this.event.requirement.maxAge = this.editMaxAge;
      });
    }

    await this.sleep(250);

    if(this.editGender){
      this.eventService.editGender(this.event.id, this.editGender).subscribe(() => {
        this.event.requirement.gender = this.editGender;
      })
    }

    await this.sleep(250);

    if(this.editPrice){
      this.eventService.editPrice(this.event.id, this.editPrice).subscribe(() => {
        this.event.price = this.editPrice;
      })
    }

    this.editar = false;

  }

  joinToEvent(idEvent) {
    this.eventService.joinToEvent(idEvent).subscribe(() => this.ngOnInit());
  }

  removeParticipant(idEvent, idUser) {
    this.eventService.removeParticipant(idEvent, idUser).subscribe(() => this.ngOnInit());
  }

  cancelRequest(idEvent) {
    this.eventService.cancelRequest(idEvent).subscribe(() => this.ngOnInit());
  }

  removeEvent(idEvent) {
    this.eventService.deleteEvent(idEvent).subscribe(() => this.router.navigate(['home']));
  }

  rateParticipants() {
    for (let u of this.event.participants) {
      if (u.reputationParticipant != 0 && u.id != this.idUser) {
        this.rateService.rateParticipant(u.id, this.event.id, u.reputationParticipant).subscribe();
        console.log("Nombre: " + u.firstName + ", score: " + u.reputationParticipant);
      }
    }
  }

  getEventComments(){
    for(let p of this.event.participants){
      if(p.id === this.idUser){
        this.eventService.getEventComments(this.event.id).subscribe(
          data => {
            this.event.userComments = data;
          });
      }
    }
  }

  rateOrganizer() {
    console.log(this.scoreOrganizer);
    if (this.scoreOrganizer > 0) {
      this.rateService.rateOrganizer(this.event.organizer.id, this.event.id, this.scoreOrganizer).subscribe();
    }
  }

  publishComment() {
    if (this.commentEvent) {
      this.eventService.publishEvent(this.event.id, this.commentEvent).subscribe(
        result => this.ngOnInit(),
        error => this.errorMessage = "Comentario demasiado largo");
      (<HTMLInputElement>document.getElementById('textarea-comentario')).value = '';
    }
  }

  deleteComment(idComment){
    this.eventService.deleteComment(idComment).subscribe(() => this.ngOnInit());
  }

  finishEvent() {
    this.eventService.finishEvent(this.idEvent).subscribe(() => this.ngOnInit());
  }

  /** FUNCIONES **/

  actualUserIsParticipant(): boolean {
    for (let i of this.event.participants) {
      if (i.id == this.idUser) {
        return true;
      }
    }
    return false;
  }

  actualUserIsApplicant(): boolean {
    for (let i of this.event.applicants) {
      if (i.id == this.idUser) {
        return true;
      }
    }
    return false;
  }

  calculateAge(): number {
    const today = new Date();
    const birthday = new Date(this.tokenStorageService.getUser().user.birthday);

    let age = today.getFullYear() - birthday.getFullYear();
    const m = today.getMonth() - birthday.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthday.getDate())) {
      age--;
    }
    return age;
  }

  cumplesLosRequisitos() {
    var age = this.calculateAge();

    if (this.event.requirement.minAge > 0 && age < this.event.requirement.minAge) {
      return false;
    }
    if (this.event.requirement.maxAge > 0 && age > this.event.requirement.maxAge) {
      return false;
    }
    if (this.event.requirement.gender && this.event.requirement.gender.toUpperCase() != this.tokenStorageService.getUser().user.gender.toUpperCase()) {
      return false;
    }
    if (this.event.requirement.reputation && this.event.requirement.reputation > this.tokenStorageService.getUser().user.reputationParticipant) {
      return false;
    }

    return true;
  }

  isNotFull(): boolean{
    if(this.event.maxParticipants > 0){
      if(this.event.maxParticipants <= this.event.participants.length){
        return false;
      }
    }
    return true;
  }

  getUserPicture(picture) {
    if (picture == null) {
      return "http://placehold.it/45x45";
    }
    return picture;
  }

  getRequirements(event: Event) {
    for (let r in event.requirement) {
      this.requirements.push(r);
    }
    return this.requirements;
  }

  isOrganizer(): boolean {
    if (this.event.organizer.id === this.idUser) {
      return true;
    }
    else false;
  }

  isParticipant(): boolean{
    for(let p of this.event.participants){
      if(p.id === this.idUser){
        return true;
      }
    }
    return false;
  }

  editEvent() {
    this.editar = !this.editar;
  }

  sortBy() {
    return this.event.userComments.sort((a, b) => new Date(b.createAt).getTime() - new Date(a.createAt).getTime());
  }

  sleep(msec) {
    return new Promise(resolve => setTimeout(resolve, msec));
  }

  checkCheckBoxvalue(gender: string) {
    this.editGender = gender;
  }
}
