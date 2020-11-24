import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators, FormBuilder } from "@angular/forms";

import { AuthService } from '../../services/auth.service';
import { TokenStorageService } from '../../services/token-storage.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = null;

  constructor(
    private router: Router,
    private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {

    this.loginForm = this.formBuilder.group(
      {
        email: new FormControl('', [Validators.required]),
        password: new FormControl('', [Validators.required]),
      }
    );

    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
    }
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }

  onSubmit(): void {

    this.loginForm.markAllAsTouched();

    if (!this.loginForm.invalid) {
      this.authService.login(this.loginForm.get('email').value, this.loginForm.get('password').value).subscribe(
        data => {
          this.tokenStorage.saveToken(data.accessToken);
          this.tokenStorage.saveUser(data);

          this.isLoginFailed = false;
          this.isLoggedIn = true;
          this.router.navigateByUrl('/home');
        },
        err => {
          this.errorMessage = "Email o contraseña incorrectas";
          this.isLoginFailed = true;
        }
      );
    } else {
      if (this.loginForm.get('email').invalid) {
        this.errorMessage = 'Email requerido';
      } else if (this.loginForm.get('password')) {
        this.errorMessage = 'Contraseña requerida';
      }
    }
  }

}
