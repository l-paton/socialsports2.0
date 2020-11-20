import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from './../../environments/environment';

const AUTH_API = environment.baseUrl + '/auth';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http:HttpClient) { }

  login(email:string, password:string): Observable<any>{
    return this.http.post(AUTH_API + "/signin", {email: email, password: password}, httpOptions);
  }

  signup(email:string, password: string, firstname: string, lastname: string, gender: string, birthday: string): Observable<any>{
    return this.http.post(AUTH_API + "/signup", {email: email, password: password, firstname: firstname, lastname: lastname, gender: gender, birthday: birthday}, httpOptions);
  }
}
