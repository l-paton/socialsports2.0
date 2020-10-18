import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../../services/token-storage.service'
import { Router, NavigationStart } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  
  showHead: boolean = false;

  constructor(private router: Router, private tokenStorage:TokenStorageService) {
    router.events.forEach((event) => {
      if(tokenStorage.isLoggedIn){
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
}
