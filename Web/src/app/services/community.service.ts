import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenStorageService } from './token-storage.service';
import { Observable } from 'rxjs';
import { User } from './../models/User';
import { environment } from './../../environments/environment';

const USER_API = environment.baseUrl + '/user';

@Injectable({
  providedIn: 'root'
})
export class CommunityService {

  constructor(private http:HttpClient, private tokenStorage:TokenStorageService) { }

  getListUsers(): Observable<User[]>{
    return this.http.get<User[]>(USER_API + '/list', { headers: new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken()})});
  }
}
