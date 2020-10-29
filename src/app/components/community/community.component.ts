import { Component, OnInit } from '@angular/core';
import { CommunityService } from '../../services/community.service';
import { UserService } from '../../services/user.service';
import { User } from '../../models/User';
import { TokenStorageService } from '../../services/token-storage.service';

@Component({
  selector: 'app-community',
  templateUrl: './community.component.html',
  styleUrls: ['./community.component.css']
})
export class CommunityComponent implements OnInit {

  users:User[];
  myFriends:User[];
  id:number;

  constructor(private communityService:CommunityService, private tokenStorageService:TokenStorageService, private userservice:UserService) { }

  ngOnInit(): void {
    this.getUsers();
    this.id = this.tokenStorageService.getUser().id;
    this.userservice.getFriends().subscribe(data => this.myFriends = data);
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
        this.ngOnInit();
      }
    );
  }

  deleteFriend(id:number){
    this.communityService.deleteFriend(id).subscribe(
      data =>{
        this.ngOnInit();
      }
    );
  }

  getUrlPictureUser(user:User): string{
    if(user.picture == null || user.picture.length <= 0){
      return "http://placehold.it/95x95";
    }
    return user.picture;
  }

  IsUserMyFriend(id:number): boolean{
      for(let i of this.myFriends){
        if(i.id == id){
          return true;
        }
      }
      return false;
  }
}
