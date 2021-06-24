import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthRequest } from 'src/app/dtos/auth-request';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  success = false;
  badCredentials = false;
  // Error flag
  error = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required]],
      keepLogin: [true]
    });
  }

  ngOnInit(): void {}

  /**
   * Perform login after submitting the form to sign in.
   */
  login() {
    if (this.loginForm.valid) {
      const authObj: AuthRequest = new AuthRequest(
        this.loginForm.value.email,
        this.loginForm.value.password
      );

      const checked = this.loginForm.value.keepLogin;

      this.authService.loginUser(authObj, checked).subscribe(
        () => {
          this.router.navigate(['/user']);
        },
        error => {
          this.defaultServiceErrorHandling(error);
          if (error.status === 401) {
            this.badCredentials = true;
          } else if (error.status === 403) {
            this.router.navigate(['/banned']);
          }
        }
      );
    } else {
      this.errorMessage = 'Invalid input';
      this.error = true;
    }
  }

  /**
   * Send reset email
   */
  reset() {
    const email = this.loginForm.value.email;
    this.error = false;
    this.userService.resetPassword(email).subscribe(
      response => {
        this.success = true;
        this.badCredentials = false;
      }, error => {
        console.error(error);
      }
    );
  }

  vanishAlert(): void {
    this.error = false;
    this.success = false;
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }
}
