import { Component, OnInit } from '@angular/core';
import { CommunityService } from '../../services/community.service';
import { User } from '../../models/User';
import { TokenStorageService } from '../../services/token-storage.service';

@Component({
  selector: 'app-community',
  templateUrl: './community.component.html',
  styleUrls: ['./community.component.css']
})
export class CommunityComponent implements OnInit {

  users:User[];
  id:number;

  constructor(private communityService:CommunityService, private tokenStorageService:TokenStorageService) { }

  ngOnInit(): void {
    this.getUsers();
    this.id = this.tokenStorageService.getUser().id;
  }

  getUsers(){
    this.communityService.getListUsers().subscribe(
      data => { 
        this.users = data;
      }
    );
  }

  addFriend(id:number){
    this.communityService.addFriend(id).subscribe(
      data => {
        
      }
    );
  }

  getUrlPictureUser(user:User): string{
    if(user.picture == null){
      return "http://placehold.it/95x95";
    }
    return user.picture;
  }

}
