import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Event } from '../../app/models/Event';
import { TokenStorageService } from './token-storage.service';

const EVENT_API = 'http://localhost:8080/api/event';

@Injectable({
  providedIn: 'root'
})

export class EventService {

  constructor(private http:HttpClient, private tokenStorage:TokenStorageService) { }

  getListEvents(): Observable<Event[]>{
    return this.http.get<Event[]>(EVENT_API + '/list', { headers: new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken()})});
  }

  createEvent(event : Event): Observable<Response>{
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());
    headers.append('content-type', 'application/json');
    return this.http.post<Response>(EVENT_API + '/create', event, {headers});
  } 

  joinToEvent(id:number): Observable<Response>{
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());
    return this.http.post<Response>(EVENT_API + '/join/' + id, {headers});
  }

  cancelRequest(id:number){
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());
    return this.http.delete(EVENT_API + '/cancel/' + id, {headers});
  }

  deleteEvent(id:number): Observable<Response>{
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());
    return this.http.delete<Response>(EVENT_API + '/delete/' + id, {headers});
  }

  leaveEvent(id:number){
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());
    return this.http.delete(EVENT_API + '/leave/' + id, {headers});
  }

  getEvent(id:string): Observable<Event>{
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());
    return this.http.get<Event>(EVENT_API + '/get/' + id, {headers});
  }

  getRequests(): Observable<any>{
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());
    return this.http.get<any>(EVENT_API + '/requests', {headers});
  }

}
