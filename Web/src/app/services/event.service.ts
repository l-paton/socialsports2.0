import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from './token-storage.service';
import { Event } from '../models/Event';
import { JsonPipe } from '@angular/common';

const EVENT_API = 'http://localhost:8080/api/event';

@Injectable({
  providedIn: 'root'
})

export class EventService {

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) { }

  getListEvents(): Observable<Event[]> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<Event[]>(EVENT_API + '/list', { headers: headers });
  }

  createEvent(event: Event): Observable<Response> {
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());
    headers.append('content-type', 'application/json');
    return this.http.post<Response>(EVENT_API + '/create', event, { headers });
  }

  joinToEvent(id) {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().set('id', id);
    return this.http.post(EVENT_API + '/join/', params, { headers: headers });
  }

  cancelRequest(id: number) {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.delete(EVENT_API + '/cancel/' + id, { headers });
  }

  deleteEvent(id: number): Observable<Response> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.delete<Response>(EVENT_API + '/delete/' + id, { headers });
  }

  leaveEvent(id: number) {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.delete(EVENT_API + '/leave/' + id, { headers });
  }

  getEvent(id: string): Observable<Event> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<Event>(EVENT_API + '/get/' + id, { headers });
  }

  getRequests(): Observable<Event[]> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<Event[]>(EVENT_API + '/requests', { headers });
  }

  searchEvents(sport: string, startDate: string, time: string, address: string): Observable<Event[]> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append('sport', sport).append('startDate', startDate).append('time', time).append('address', address);
    return this.http.get<Event[]>(EVENT_API + '/search', { headers: headers, params: params });
  }

  getEventsByOrganizer(): Observable<Event[]> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<Event[]>(EVENT_API + '/created', { headers });
  }
  acceptUserRequest(idEvent, idUser) {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append('idEvent', idEvent).append('idUser', idUser);
    return this.http.post(EVENT_API + '/accept', params, { headers: headers });
  }

  cancelUserRequest(idEvent, idUser){
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append('idEvent', idEvent).append('idUser', idUser);
    return this.http.post(EVENT_API + '/deny', params, { headers: headers });
  }

  editStartDate(id, startDate){
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().set('id', id).set('startDate', startDate);
    return this.http.put(EVENT_API + '/edit/startdate', params, {headers: headers});
  }

  editTime(id, time){
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().set('id', id).set('time', time);
    return this.http.put(EVENT_API + '/edit/time', params, {headers: headers});
  }

  editMaxParticipants(id, maxParticipants){
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().set('id', id).set('maxParticipants', maxParticipants);
    return this.http.put(EVENT_API + '/edit/maxparticipants', params, {headers: headers});
  }

}
