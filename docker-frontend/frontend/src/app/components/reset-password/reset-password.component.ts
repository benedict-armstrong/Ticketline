import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
  token: string;
  error = false;
  passwordError = false;
  passwordErrorMessage = '';
  passwordSuccess = false;
  passwordSubmitted = false;

  passwordChangeForm: FormGroup;

  constructor(private router: Router, private userService: UserService, private route: ActivatedRoute, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params.token === undefined) {
        this.router.navigate(['/']);
      }

      this.token = params.token;
    });

    this.passwordChangeForm = this.formBuilder.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  vanishAlert(): void {
    this.error = false;
    this.passwordSuccess = false;
    this.passwordError = false;
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
        const password = this.passwordChangeForm.value.password;

        this.userService.changePassword(password, this.token).subscribe(
          () => {
            this.passwordSubmitted = false;
            this.passwordChangeForm.reset();
            this.passwordSuccess = true;
            setTimeout(() => {
              this.router.navigate(['/login']);
            }, 3000);
          },
          (error) => {
            this.defaultPasswordErrorHandling(error);
          }
        );
      }
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
