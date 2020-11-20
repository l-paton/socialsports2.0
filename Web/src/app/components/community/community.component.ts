import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../../services/token-storage.service';
import { CommunityService } from '../../services/community.service';
import { FriendshipService } from '../../services/friendship.service';
import { User } from '../../models/User';


@Component({
  selector: 'app-community',
  templateUrl: './community.component.html',
  styleUrls: ['./community.component.css']
})
export class CommunityComponent implements OnInit {

  users: User[] = [];
  myFriends: User[] = [];
  myFriendRequestsSent: User[] = [];
  myFriendRequestsReceived: User[] = [];
  id: number;

  constructor(
    private communityService: CommunityService,
    private tokenStorageService: TokenStorageService,
    private friendshipService: FriendshipService) {
  }

  ngOnInit(): void {
    this.getUsers();
    this.id = this.tokenStorageService.getUser().user.id;
    this.friendshipService.getMyFriends().subscribe(data => this.myFriends = data);
    this.friendshipService.getRequestsSent().subscribe(data => this.myFriendRequestsSent = data);
    this.friendshipService.getRequestsReceived().subscribe(data => this.myFriendRequestsReceived = data);
  }

  getUsers() {
    this.communityService.getListUsers().subscribe(
      data => {
        this.users = data;
      }
    );
  }

  sendFriendRequest(id: number){
    this.friendshipService.sendFriendRequest(id).subscribe(() => this.ngOnInit());
  }

  cancelRequest(id: number){
    this.friendshipService.cancelRequest(id).subscribe(() => this.ngOnInit());
  }

  acceptFriendRequest(id: number){
    this.friendshipService.acceptFriend(id).subscribe(() => this.ngOnInit());
  }

  deleteFriend(id: number) {
    this.friendshipService.deleteFriend(id).subscribe(() => this.ngOnInit());
  }

  IsUserMyFriend(id: number): boolean {
    for (let i of this.myFriends) {
      if (i.id == id) {
        return true;
      }
    }
    return false;
  }

  IsInMyRequestsSent(id: number): boolean{
    for(let i of this.myFriendRequestsSent){
      if(i.id == id){
        return true;
      }
    }
    return false;
  }

  IsInMyRequestsReceived(id: number): boolean{
    for(let i of this.myFriendRequestsReceived){
      if(i.id == id){
        return true;
      }
    }
    return false;
  }
}
