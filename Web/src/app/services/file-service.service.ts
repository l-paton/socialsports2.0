import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHeaders } from '@angular/common/http';
import { TokenStorageService } from '../services/token-storage.service';
import { Observable } from 'rxjs';
import { environment } from './../../environments/environment';

const FILE_API = environment.baseUrl + '/images';

@Injectable({
  providedIn: 'root'
})
export class FileServiceService {

  constructor(private http:HttpClient, private tokenStorage:TokenStorageService) { }

  uploadImage(file:File): Observable<any>{
    const formData: FormData = new FormData();
    formData.append('file', file);
    
    let headers = new HttpHeaders();
    headers.append('Authorization: ', 'bearer ' + this.tokenStorage.getToken());

    return this.http.post<any>(FILE_API + '/upload', formData, {headers});
  }
}
