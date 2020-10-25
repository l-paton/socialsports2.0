import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../../services/token-storage.service'
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  
  showHead: boolean = false;
  picture:string;

  constructor(private router: Router, private tokenStorage:TokenStorageService, private userService:UserService) {
    this.router.events.forEach(() => {
      if(tokenStorage.isLoggedIn){
        this.userService.getProfilePicture().subscribe(data => this.picture = data);
        this.showHead = true;
      }else{
        this.showHead = false;
      }
    });
   }

  ngOnInit(): void {
    
  }

  logout(){
    this.tokenStorage.logout();
  }

  getProfilePicture(): string{
    if(this.picture != undefined){
      this.ngOnInit();
      if(this.picture == null){
        return "http://placehold.it/45x45";
      }
      return this.picture;
    }
  }
}
