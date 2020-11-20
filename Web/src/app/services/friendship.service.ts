import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { TokenStorageService } from './token-storage.service';
import { User } from '../models/User';
import { Observable } from 'rxjs';
import { environment } from './../../environments/environment';

const FRIEND_API = environment.baseUrl + '/friend';

@Injectable({
  providedIn: 'root'
})
export class FriendshipService {

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) { }

  sendFriendRequest(id){
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append("id", id);
    return this.http.post(FRIEND_API + '/sendrequest', params, {headers: headers});
  }

  cancelRequest(id){
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append("id", id);
    return this.http.post(FRIEND_API + '/cancelrequest', params, {headers: headers});
  }

  acceptFriend(id){
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append("id", id);
    return this.http.post(FRIEND_API + '/accept', params, {headers: headers});
  }

  denyFriend(id){
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append("id", id);
    return this.http.post(FRIEND_API + '/deny', params, {headers: headers});
  }

  deleteFriend(id){
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.delete(FRIEND_API + '/delete/' + id, {headers: headers});
  }

  getMyFriends(): Observable<User[]>{
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<User[]>(FRIEND_API + '/list', {headers: headers});
  }

  getUserFriends(id): Observable<User[]>{
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<User[]>(FRIEND_API + '/list/' + id, {headers: headers});
  }

  getRequestsSent(): Observable<User[]>{
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<User[]>(FRIEND_API + '/requests/sent', {headers: headers});
  }

  getRequestsReceived(): Observable<User[]>{
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    return this.http.get<User[]>(FRIEND_API + '/requests/received', {headers: headers});
  }
}