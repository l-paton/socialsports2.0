import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormGroup, FormControl, Validators, FormBuilder } from "@angular/forms";
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signUpForm: FormGroup;
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = null;
  gender: string;

  constructor(private authService: AuthService, private router: Router, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.signUpForm = this.formBuilder.group(
      {
        firstname: new FormControl('', [Validators.required]),
        lastname: new FormControl('', [Validators.required]),
        email: new FormControl('', [Validators.required]),
        password: new FormControl('', [Validators.required]),
        repeatPassword: new FormControl('', [Validators.required]),
      }
    );
  }

  get firstname() { return this.signUpForm.get('firstname') }
  get lastname() { return this.signUpForm.get('lastname') }
  get email() { return this.signUpForm.get('email') }
  get password() { return this.signUpForm.get('password') }
  get repeatPassword() { return this.signUpForm.get('repeatPassword') }

  onSubmit(): void {

    if (!this.signUpForm.invalid) {

      if (this.password.value === this.repeatPassword.value) {
        this.authService.signup(this.email.value, this.password.value, this.firstname.value, this.lastname.value, this.gender).subscribe(
          () => {
            this.isSuccessful = true;
            this.isSignUpFailed = false;
            this.router.navigateByUrl('/login');
          },
          err => {
            console.log(err);
            this.errorMessage = err.error;
            this.isSignUpFailed = true;
          }
        );
      } else {
        this.errorMessage = 'Las contraseñas no coinciden';
      }
    } else {
      this.errorMessage = 'Rellena bien los campos';
    }
  }

  checkCheckBoxvalue(gender: string) {
    console.log(gender);
    this.gender = gender;
  }
}