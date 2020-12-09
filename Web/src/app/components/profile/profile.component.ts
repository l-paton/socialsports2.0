import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/User';
import { Event } from 'src/app/models/Event';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user.service';
import { TokenStorageService } from '../../services/token-storage.service';
import { FileServiceService } from '../../services/file-service.service';
import { FriendshipService } from '../../services/friendship.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: User;
  friends: User[];
  eventsJoined: Event[];

  email: string;
  firstName: string;
  lastName: string;
  address: string;
  description: string;
  selectedFiles: FileList;
  birthDay: Date;
  gender: string;
  id: number;
  editarUsuario: boolean = false;
  idUser: string;
  userSubscription: Subscription;

  constructor(
    private route: ActivatedRoute, 
    private userService: UserService, 
    private tokenStorage: TokenStorageService, 
    private friendshipService: FriendshipService,
    private fileService: FileServiceService) {
  }

  ngOnInit(): void {
    this.id = this.tokenStorage.getUser().user.id;
      this.route.paramMap.subscribe(params => {
        this.idUser = params.get('id');
        this.userService.getUser(this.idUser).subscribe(data => this.user = data);
        this.friendshipService.getUserFriends(this.idUser).subscribe(data => this.friends = data);
        this.userService.getEventsJoined(this.idUser).subscribe(data => this.eventsJoined = data);
      });
  }

  async modifyProfile() {
    if (this.description != null) {
      this.userService.modifyDescription(this.description).subscribe(() => {
        this.user.description = this.description;
        this.tokenStorage.saveUser({"user": this.user});
      });
    }
    
    await this.sleep(500);

    if (this.firstName != null && this.firstName != "") {
      this.userService.modifyFirstName(this.firstName).subscribe(() => {
        this.user.firstName = this.firstName;
        this.tokenStorage.saveUser({"user": this.user});
      });
    }
    
    await this.sleep(500);
    
    if (this.lastName != null && this.lastName != "") {
      this.userService.modifyLastName(this.lastName).subscribe(() => {
        this.user.lastName = this.lastName;
        this.tokenStorage.saveUser({"user": this.user});
      });
    }

    await this.sleep(500);

    if (this.address != null && this.address != "") {
      this.userService.modifyAddress(this.address).subscribe(() => {
        this.user.address = this.address;
        this.tokenStorage.saveUser({"user": this.user});
      });
    }
    
    await this.sleep(500);
    
    if(this.birthDay){
      this.userService.modifyBirthday(this.birthDay.toString()).subscribe(() => {
        this.user.birthday = this.birthDay;
        this.tokenStorage.saveUser({"user": this.user});
      });
    }
    
    await  this.sleep(500);
    
    if(this.gender){
      this.userService.modifyGenre(this.gender).subscribe(() => {
        this.user.gender = this.gender;
        this.tokenStorage.saveUser({"user": this.user});
      });
    }
    
    await this.sleep(500);
    
    if (this.selectedFiles && this.selectedFiles.length > 0) {
      this.fileService.uploadImage(this.selectedFiles.item(0)).subscribe();
    }

    await this.sleep(500);
    
    window.location.reload();

  }

  sleep(msec) {
    return new Promise(resolve => setTimeout(resolve, msec));
}

  /** FUNCIONES **/

  getPicture() {
    if (this.user != undefined) {
      if (this.user.picture == null) {
        return "http://placehold.it/120x120";
      }
      return this.user.picture;
    }
  }

  getFriendPicture(picture) {
    if (picture == null) {
      return "http://placehold.it/45x45";
    }
    return picture;
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  isOrganizer(): boolean{

    if(this.user.id === this.id){
      return true;
    }
    else false;
  }

  editUser(){
    this.editarUsuario = !this.editarUsuario;
  }

  selectChangeHandler(event: any){
    this.gender = event.target.value;
  }
  
  deleteFriend(id: number) {
    this.friendshipService.deleteFriend(id).subscribe(() => this.ngOnInit());
  }
}
