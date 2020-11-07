import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { TokenStorageService } from '../../services/token-storage.service'
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { EventService } from '../../services/event.service';
import { User } from 'src/app/models/User';

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
  applicants: User[] = [];

  constructor(
    private router: Router,
    private cdRef: ChangeDetectorRef,
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

  ngAfterViewChecked(){
    this.showNotifications();
    this.cdRef.detectChanges();
  }

  getNotifications(){
    this.eventService.getRequests().subscribe(data => {this.jsonNotifications = data});
  }

  showNotifications(): number{
    var n = 0;
    this.applicants = [];

    for(let key in this.jsonNotifications){  
      this.applicants.push(this.jsonNotifications[key] as User);
      n++;
    }
    this.notifications = n;
    return n;
  }

  logout() {
    this.tokenStorage.logout();
  }

  isActive(path){
    var url = this.router.url;

    if(path == '/'){
      if(url === path || url === "/home"){
        return true;
      }
    }else if(url === path){
      return true;
    }
    
    return false;
  }

}
