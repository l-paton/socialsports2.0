import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { UserService } from './../../services/user.service';
import { TokenStorageService } from './../../services/token-storage.service';

@Component({
  selector: 'app-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.css']
})
export class ConfigurationComponent implements OnInit {

  password: string;
  repeatPassword: string;
  errorMessage: string;

  constructor(private userService: UserService, private tokenStorageService: TokenStorageService, @Inject(DOCUMENT) document) { }

  ngOnInit(): void {
  }

  modifyPassword(){
    (<HTMLInputElement>document.getElementById('inputPassword1')).value = '';
    (<HTMLInputElement>document.getElementById('inputPassword2')).value = '';
    if(this.password && this.repeatPassword){
      if(this.password === this.repeatPassword){
        this.userService.modifyPassword(this.password).subscribe(
          () => this.ngOnInit(), 
          err => {
          this.errorMessage = err.error;
        });
      }else{
        this.errorMessage = "Las contraseñas no son iguales";
      }
    }else{
      this.errorMessage = "Rellena los campos";
    }
  }

  deleteAccount(){
    this.userService.deleteAccount().subscribe(() => 
    {
      this.tokenStorageService.logout();
      window.location.reload();
    });
  }

}
