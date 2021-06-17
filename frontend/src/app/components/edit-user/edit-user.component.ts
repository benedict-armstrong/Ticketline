import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { Address } from 'src/app/dtos/address';
import { User } from '../../dtos/user';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss'],
})
export class EditUserComponent implements OnInit {
  editUserForm: FormGroup;
  oldUser: User;
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
    private actRoute: ActivatedRoute,
    private router: Router
  ) {
    this.editUserForm = this.formBuilder.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      telephoneNumber: [''],
      email: [{value: '', disabled: true}, [Validators.required]],
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
  updateUser() {
    this.submitted = true;
    if (this.editUserForm.valid) {
      const user: User = new User(
        this.oldUser.id,
        this.editUserForm.value.firstName,
        this.editUserForm.value.lastName,
        this.editUserForm.value.telephoneNumber,
        this.oldUser.email,
        this.oldUser.password,
        this.oldUser.lastLogin,
        this.oldUser.lastReadNewsId,
        this.editUserForm.value.points,
        this.editUserForm.value.status,
        this.editUserForm.value.role,
        new Address(
          this.oldUser.address.id,
          this.editUserForm.value.addressName,
          this.editUserForm.value.lineOne,
          this.editUserForm.value.lineTwo,
          this.editUserForm.value.city,
          this.editUserForm.value.postcode,
          this.editUserForm.value.country,
          this.oldUser.address.eventLocation
        )
      );

      this.userService.updateUser(user).subscribe(
        () => {
          this.success = true;
          setTimeout(() => {
            this.router.navigate(['/users']);
          }, 3000);
        },
        (error) => {
          this.defaultServiceErrorHandling(error);
        }
      );
    } else {
      console.log('Invalid input');
    }
  }

  setDetails() {
    this.editUserForm = this.formBuilder.group({
      firstName: [this.oldUser.firstName, [Validators.required]],
      lastName: [this.oldUser.lastName, [Validators.required]],
      telephoneNumber: [this.oldUser.telephoneNumber],
      email: [{value: this.oldUser.email, disabled: true}, [Validators.required]],
      points: [this.oldUser.points, [Validators.min(0)]],
      status: ['ACTIVE', [Validators.required]],
      role: ['CLIENT', [Validators.required]],
      addressName: [this.oldUser.address.name, [Validators.required]],
      lineOne: [this.oldUser.address.lineOne, [Validators.required]],
      lineTwo: [this.oldUser.address.lineTwo],
      city: [this.oldUser.address.city, [Validators.required]],
      postcode: [this.oldUser.address.postcode, [Validators.required]],
      country: [this.oldUser.address.country, [Validators.required]],
    });
  }

  vanishAlert(): void {
    this.error = false;
    this.success = false;
  }

  ngOnInit(): void {
    const userId = this.actRoute.snapshot.params.id;
    this.userService.getUserById(userId).subscribe(
      (response) => {
        this.oldUser = response;
        this.setDetails();
        console.log(this.oldUser);
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }

    );
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
