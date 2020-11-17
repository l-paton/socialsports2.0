import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from './token-storage.service';
import { User } from '../models/User';
import { Event } from '../models/Event';

const USER_API = 'http://localhost:8080/api/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) { }

  getUser(id): Observable<User>{
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<User>(USER_API + "/profile/" + id, { headers });
  }

  getProfilePicture(): Observable<any> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get(USER_API + '/picture', { headers, responseType: 'text' });
  }

  getUserData(): Observable<User> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<User>(USER_API + "/data", { headers });
  }

  /** EDIT USER */

  modifyFirstName(firstName: string): Observable<any> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append('firstName', firstName);
    return this.http.put(USER_API + '/edit/firstname', params, { headers });
  }

  modifyLastName(lastName: string): Observable<any> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append('lastName', lastName);
    return this.http.put(USER_API + '/edit/lastname', params, { headers });
  }

  modifyAddress(address: string): Observable<any> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append('address', address);
    return this.http.put(USER_API + '/edit/address', params, { headers });
  }

  modifyBirthday(birthday): Observable<any>{
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append('birthday', birthday);
    return this.http.put(USER_API + '/edit/birthday', params, { headers });
  }

  modifyGenre(gender): Observable<any>{
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append('gender', gender);
    return this.http.put(USER_API + '/edit/gender', params, { headers });
  }

  modifyDescription(description: string): Observable<any> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.put(USER_API + '/edit/description', description, { headers });
  }

  /** EVENTS */

  getEventsJoined(id): Observable<Event[]> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<Event[]>(USER_API + '/events/joined/' + id, { headers });
  }

  getEventsApplied(): Observable<Event[]> {
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<Event[]>(USER_API + '/events/applied', { headers });
  }
}
