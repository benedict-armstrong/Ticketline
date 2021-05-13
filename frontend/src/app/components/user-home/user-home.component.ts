import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/dtos/user';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.scss'],
})
export class UserHomeComponent implements OnInit {
  // Error flag
  error = false;
  errorMessage = '';
  passwordError = false;
  passwordErrorMessage = '';
  // Success Flag
  passwordSuccess = false;

  // Show User Actions
  userActions = false;

  // If password change is submitted
  passwordSubmitted = false;

  user: User;
  userRole: string;
  passwordChangeForm: FormGroup;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
    if (this.authService.isLoggedIn()) {
      this.loadUser(this.authService.getUserEmail());
    } else {
      this.router.navigate(['']);
    }

    this.passwordChangeForm = this.formBuilder.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  ngOnInit(): void {}

  /**
   * Load User with email from Backend.
   */
  loadUser(email: string) {
    this.userService.getUserByEmail(email).subscribe(
      (response) => {
        this.user = response;
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Password change
   */
  changePassword() {
    this.passwordSubmitted = true;
    if (this.passwordChangeForm.valid) {
      if (
        this.passwordChangeForm.value.password ===
        this.passwordChangeForm.value.passwordRepeat
      ) {
        this.user.password = this.passwordChangeForm.value.password;

        this.userService.updateUser(this.user).subscribe(
          () => {
            this.passwordSubmitted = false;
            this.passwordChangeForm.reset();
            this.passwordSuccess = true;
          },
          (error) => {
            this.defaultPasswordErrorHandling(error);
          }
        );
      }
    }
  }

  vanishAlert(): void {
    this.error = false;
    this.passwordSuccess = false;
    this.passwordError = false;
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

  private defaultPasswordErrorHandling(error: any) {
    console.log(error);
    this.passwordError = true;
    if (typeof error.error === 'object') {
      this.passwordErrorMessage = error.error.error;
    } else {
      this.passwordErrorMessage = error.error;
    }
  }
}
