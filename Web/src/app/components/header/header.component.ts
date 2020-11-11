import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { TokenStorageService } from '../../services/token-storage.service'
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { EventService } from '../../services/event.service';
import { User } from 'src/app/models/User';
import { Event } from 'src/app/models/Event';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  id: number;
  showHead: boolean = false;
  picture: string;
  notifications: number = 0;
  jsonNotifications: JSON;
  events: Event[];
  applicants: string[];

  constructor(
    private router: Router,
    private tokenStorage: TokenStorageService,
    private userService: UserService,
    private eventService: EventService) {
  }

  ngOnInit(): void {
    this.router.events.forEach(() => {
      if (this.tokenStorage.isLoggedIn) {
        this.id = this.tokenStorage.getUser().user.id;
        this.userService.getProfilePicture().subscribe(data => this.picture = data);
        this.getNotifications();
        this.showHead = true;
      } else {
        this.showHead = false;
      }
    });
  }

  getNotifications(){
    this.eventService.getRequests().subscribe(data => {this.events = data});
  }

  showNotifications(): number{
    var n = 0;

    this.events.forEach(o => {
        n += o.applicants.length;
      }
    );

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

  acceptUserRequest(idEvent: string, idUser: string){
    this.eventService.acceptUserRequest(idEvent, idUser).subscribe(() => this.ngOnInit());
  }

  cancelUserRequest(idEvent: string, idUser: string){
    this.eventService.cancelUserRequest(idEvent, idUser).subscribe(() => this.ngOnInit());
  }

  getProfile(id: number) {
    this.router.navigate(['/profile', id]);
  }

}
