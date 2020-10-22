import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormGroup, FormControl, Validators, FormBuilder } from "@angular/forms";
import {Router} from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signUpForm: FormGroup;
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(private authService:AuthService, private router:Router, private formBuilder:FormBuilder) { }

  ngOnInit(): void {
    this.signUpForm = this.formBuilder.group(
      {
        firstname:new FormControl('', [Validators.required]),
        lastname:new FormControl('', [Validators.required]),
        email:new FormControl('', [Validators.required]),
        password:new FormControl('', [Validators.required]),
        repeatPassword:new FormControl('', [Validators.required])
      }
    );
  }

  get firstname(){ return this.signUpForm.get('firstname') }
  get lastname(){ return this.signUpForm.get('lastname') }
  get email(){ return this.signUpForm.get('email') }
  get password(){ return this.signUpForm.get('password') }
  get repeatPassword(){ return this.signUpForm.get('repeatPassword') }

  onSubmit(): void {

    this.signUpForm.markAllAsTouched();

    if (!this.signUpForm.invalid) {
      this.authService.signup(this.signUpForm).subscribe(
        data => {
          this.isSuccessful = true;
          this.isSignUpFailed = false;
          this.router.navigateByUrl('/login');
        },
        err => {
          this.errorMessage = err.error.message;
          this.isSignUpFailed = true;
        }
      );
    }
  }
}