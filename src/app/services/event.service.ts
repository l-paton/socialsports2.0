import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Event } from '../../app/models/Event';

const EVENT_API = 'http://localhost:8080/api/event';

@Injectable({
  providedIn: 'root'
})

export class EventService {

  constructor(private http:HttpClient) { }

  getListEvents(): Observable<Event[]>{
    return this.http.get<Event[]>(EVENT_API + "/list");
  }
}
