import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenStorageService } from './../services/token-storage.service'

@Injectable({
  providedIn: 'root'
})
export class HideGuard implements CanActivate {

  constructor(private router:Router, private tokenStorage:TokenStorageService){}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if(this.tokenStorage.isLoggedIn){
      this.router.navigate(['/home']);
    }
    return true;
  }
  
}
