import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../../services/token-storage.service'
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { EventService } from '../../services/event.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  showHead: boolean = false;
  picture: string;
  notifications: number = 0;
  jsonNotifications: JSON;

  constructor(
    private router: Router,
    private tokenStorage: TokenStorageService,
    private userService: UserService,
    private eventService: EventService) {
  }

  ngOnInit(): void {
    this.router.events.forEach(() => {
      if (this.tokenStorage.isLoggedIn) {
        this.userService.getProfilePicture().subscribe(data => this.picture = data);
        this.getNotifications();
        this.showHead = true;
      } else {
        this.showHead = false;
      }
    });
  }

  getNotifications(){
    this.eventService.getRequests().subscribe(data => {this.jsonNotifications = data});
  }

  showNotifications(): number{
    var n = 0;
    for(let key in Object.keys(this.jsonNotifications)){  
      n++;
    }
    this.notifications = n;
    return n;
  }

  logout() {
    this.tokenStorage.logout();
  }

}
