import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  email: string;
  password: string;

  constructor(public router:Router) { }

  ngOnInit(): void {
  }

  login() {
    if(this.email == "prueba@hotmail.com" && this.password == "1234"){
      this.router.navigateByUrl('/home');
    }
  }

}
