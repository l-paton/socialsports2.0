import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Event } from '../../app/models/Event';
import { TokenStorageService } from './token-storage.service';
import { User } from '../models/User';

const USER_API = 'http://localhost:8080/api/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient, private tokenStorage:TokenStorageService) { }

  getEventsJoined(): Observable<Event[]>{
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());
    return this.http.get<Event[]>(USER_API + '/events/joined', {headers});
  }

  getProfilePicture(): Observable<any>{
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());
    return this.http.get(USER_API + '/picture', {headers, responseType: 'text'});
  }

  getUserData(): Observable<User>{
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());
    return this.http.get<User>(USER_API + "/data", {headers});
  }
}
