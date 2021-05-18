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
  // Error flag
  error = false;
  errorMessage = '';


  constructor(private authService: AuthService,
              private userService: UserService,
              private formBuilder: FormBuilder, private router: Router) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required]]
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

      this.authService.loginUser(authObj).subscribe(
        () => {
          this.router.navigate(['/user']);
        },
        error => {
          this.defaultServiceErrorHandling(error);
        }
      );
    } else {
      this.errorMessage = 'Invalid input';
      this.error = true;
    }
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
