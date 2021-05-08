import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Address } from 'src/app/dtos/address';
import { User } from '../../dtos/user';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss'],
})
export class AddUserComponent implements OnInit {
  addUserForm: FormGroup;
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
    private router: Router
  ) {
    this.addUserForm = this.formBuilder.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      telephoneNumber: [''],
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required, Validators.minLength(8)]],
      points: [0, [Validators.min(0)]],
      status: ['ACTIVE', [Validators.required]],
      role: ['CLIENT', [Validators.required]],
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
    if (this.addUserForm.valid) {
      const user: User = new User(
        null,
        this.addUserForm.value.firstName,
        this.addUserForm.value.lastName,
        this.addUserForm.value.telephoneNumber,
        this.addUserForm.value.email,
        this.addUserForm.value.password,
        new Date(),
        this.addUserForm.value.points,
        this.addUserForm.value.status,
        this.addUserForm.value.role,
        new Address(
          null,
          this.addUserForm.value.addressName,
          this.addUserForm.value.lineOne,
          this.addUserForm.value.lineTwo,
          this.addUserForm.value.city,
          this.addUserForm.value.postcode,
          this.addUserForm.value.country
        )
      );
      this.userService.createUser(user).subscribe(
        () => {
          this.success = true;
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

  ngOnInit(): void {}

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
