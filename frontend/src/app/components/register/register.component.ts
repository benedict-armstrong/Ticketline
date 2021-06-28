import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Address } from 'src/app/dtos/address';
import { User } from '../../dtos/user';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';

  // Success Flag
  success = false;

  constructor(
    private userService: UserService,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.formBuilder.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      telephoneNumber: [''],
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required, Validators.minLength(8)]],
      addressName: ['', [Validators.required]],
      lineOne: ['', [Validators.required]],
      lineTwo: [''],
      city: ['', [Validators.required]],
      postcode: ['', [Validators.required]],
      country: ['', [Validators.required]],
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  addUser() {
    this.submitted = true;
    if (this.registerForm.valid) {
      const user: User = new User(
        null,
        this.registerForm.value.firstName,
        this.registerForm.value.lastName,
        this.registerForm.value.telephoneNumber,
        this.registerForm.value.email,
        this.registerForm.value.password,
        new Date(),
        null,
        0,
        'ACTIVE',
        'CLIENT',
        new Address(
          null,
          this.registerForm.value.addressName,
          this.registerForm.value.lineOne,
          this.registerForm.value.lineTwo,
          this.registerForm.value.city,
          this.registerForm.value.postcode,
          this.registerForm.value.country,
          false
        )
      );
      this.userService.createUser(user).subscribe(
        () => {
          this.success = true;
          this.router.navigate(['/user'], { state: { user } });
        },
        (error) => {
          this.defaultServiceErrorHandling(error);
        }
      );
    } else {
      console.log('Invalid input');
    }
  }

  vanishAlert(): void {
    this.error = false;
    this.success = false;
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
    }
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
