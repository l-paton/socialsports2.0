import { Component, OnInit, SecurityContext } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { TokenStorageService } from '../../services/token-storage.service'
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { User } from 'src/app/models/User';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  
  showHead: boolean = false;
  picture:string;

  constructor(private sanitizer: DomSanitizer, private router: Router, private tokenStorage:TokenStorageService, private userService:UserService) {
    router.events.forEach(() => {
      if(tokenStorage.isLoggedIn){
        this.showHead = true;
      }else{
        this.showHead = false;
      }
    });
   }

  ngOnInit(): void {
    this.userService.getProfilePicture().subscribe(data => this.picture = data);
  }

  logout(){
    this.tokenStorage.logout();
  }

  getProfilePicture(): string{
    console.log(this.picture);
    if(this.picture == null){
      return "http://placehold.it/45x45";
    }
    return this.picture;
  }
}
