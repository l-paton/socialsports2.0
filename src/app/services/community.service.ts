import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenStorageService } from './token-storage.service';
import { Observable } from 'rxjs';
import { User } from './../models/User';

const USER_API = 'http://localhost:8080/api/user';

@Injectable({
  providedIn: 'root'
})
export class CommunityService {

  constructor(private http:HttpClient, private tokenStorage:TokenStorageService) { }

  getListUsers(): Observable<User[]>{
    return this.http.get<User[]>(USER_API + '/list', { headers: new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken()})});
  }

  addFriend(id:number): Observable<any>{
    return this.http.post<any>(USER_API + '/add/friend/' + id, { headers: new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken()})});
  }
}
