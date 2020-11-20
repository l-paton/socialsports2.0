import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { TokenStorageService } from './token-storage.service';
import { environment } from './../../environments/environment';

const RATE_API = environment.baseUrl + '/rate';

@Injectable({
  providedIn: 'root'
})
export class RateService {

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) { }

  rateParticipant(idParticipant, idEvent, score){
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append('idParticipant', idParticipant).append('idEvent', idEvent).append('score', score);
    return this.http.post(RATE_API + '/participant', params, {headers: headers});
  }

  rateOrganizer(idOrganizer, idEvent, score){
    let headers = new HttpHeaders({ 'Authorization': 'bearer ' + this.tokenStorage.getToken() });
    let params = new HttpParams().append('idOrganizer', idOrganizer).append('idEvent', idEvent).append('score', score);
    return this.http.post(RATE_API + '/organizer', params, {headers: headers});
  }
}
