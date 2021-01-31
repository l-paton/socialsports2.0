import { Component, Inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Event } from '../../models/Event';
import { TokenStorageService } from './../../services/token-storage.service';
import { EventService } from './../../services/event.service';
import { RateService } from './../../services/rate.service';
import { calculateAge, eventIsFull, isApplicant, isParticipant, meetTheRequirements } from '../../_helpers/utils';

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

    if (this.editAddress) {
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

    if (this.editComments) {
      this.eventService.editComment(this.event.id, this.editComments).subscribe(() => {
        this.event.comments = this.editComments;
      });
    }

    await this.sleep(250);

    if (this.editMinAge) {
      this.eventService.editMinAge(this.event.id, this.editMinAge).subscribe(() => {
        this.event.requirement.minAge = this.editMinAge;
      });
    }

    await this.sleep(250);

    if (this.editMaxAge) {
      this.eventService.editMaxAge(this.event.id, this.editMaxAge).subscribe(() => {
        this.event.requirement.maxAge = this.editMaxAge;
      });
    }

    await this.sleep(250);

    if (this.editGender) {
      this.eventService.editGender(this.event.id, this.editGender).subscribe(() => {
        this.event.requirement.gender = this.editGender;
      })
    }

    await this.sleep(250);

    if (this.editPrice) {
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

  getEventComments() {
    for (let p of this.event.participants) {
      if (p.id === this.idUser) {
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

  deleteComment(idComment) {
    this.eventService.deleteComment(idComment).subscribe(() => this.ngOnInit());
  }

  finishEvent() {
    this.eventService.finishEvent(this.idEvent).subscribe(() => this.ngOnInit());
  }

  isParticipant(): boolean {
    return isParticipant(this.event.participants, this.idUser);
  }

  isApplicant(): boolean {
    return isApplicant(this.event.applicants, this.idUser);
  }

  calculateAge(): number {
    return calculateAge(this.tokenStorageService.getUser().user.birthday);
  }

  meetTheRequirements() {
    var age = this.calculateAge();
    var gender = this.tokenStorageService.getUser().user.gender;
    var score = this.tokenStorageService.getUser().user.reputationParticipant;
    return meetTheRequirements(this.event, age, gender, score);
  }

  eventIsFull(): boolean {
    return eventIsFull(this.event);
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

  /*
 0. Eliminar o finalizar evento
 1. Cancelar petición
 2. Abandonar
 3. Unirse
 4. No cumples los requisitos
 */
  button(): number {

    if (this.isOrganizer()) {
      return 0;
    }

    if (this.isApplicant()) {
      return 1;
    } else if (this.isParticipant()) {
      return 2
    } else if (this.meetTheRequirements() && !this.eventIsFull()) {
      return 3;
    } else if (!this.meetTheRequirements() && !this.eventIsFull()) {
      return 4;
    } else {
      return 5;
    }
  }
}
