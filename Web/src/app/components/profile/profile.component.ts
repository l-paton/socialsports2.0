import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/User';
import { UserService } from '../../services/user.service';
import { TokenStorageService } from '../../services/token-storage.service';
import { FileServiceService } from '../../services/file-service.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: User;
  email: string;
  firstName: string;
  lastName: string;
  description: string;
  selectedFiles: FileList;

  constructor(private userService: UserService, private tokenStorage: TokenStorageService, private fileService: FileServiceService) {
  }

  ngOnInit(): void {
    this.userService.getUserData().subscribe(data => this.user = data);
    this.email = this.tokenStorage.getUser().email;
  }

  getPicture() {
    if (this.user != undefined) {
      if (this.user.picture == null) {
        return "http://placehold.it/120x120";
      }
      return this.user.picture;
    }
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  uploadImage() {
    if (this.selectedFiles.length > 0) {
      this.fileService.uploadImage(this.selectedFiles.item(0)).subscribe(() => window.location.reload());
    }
  }

  modifyProfile() {
    if (this.description != null) {
      this.userService.modifyDescription(this.description).subscribe();
    }

    if (this.firstName != null && this.firstName != "") {
      this.userService.modifyFirstName(this.firstName).subscribe();
    }

    if (this.lastName != null && this.lastName != "") {
      this.userService.modifyLastName(this.lastName).subscribe();
    }
  }

}
