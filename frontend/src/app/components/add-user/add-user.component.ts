import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApplicationUser, UserRole, Status } from '../../dtos/application-user';
import { ApplicationUserService } from '../../services/application-user.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {

  addUserForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';

  constructor(private applicationUserService: ApplicationUserService,private formBuilder: FormBuilder, private router: Router) {
    this.addUserForm = this.formBuilder.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required, Validators.minLength(8)]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      points: [0, [Validators.min(0)]],
      permissions: ['CLIENT', [Validators.required]]
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
   addUser() {
    this.submitted = true;
    if (this.addUserForm.valid) {
      var user:ApplicationUser = new ApplicationUser(null
        , this.addUserForm.value.email
        , this.addUserForm.value.password
        , this.addUserForm.value.firstName
        , this.addUserForm.value.lastName
        , this.addUserForm.value.points, null, UserRole.ADMIN, Status.ACTIVE, Date.now());
      this.applicationUserService.createUser(user).subscribe(
        () => {
          console.log('IT WORKS');
        },
        error => {
          this.defaultServiceErrorHandling(error);
        }
      );
    } else {
      console.log('Invalid input');
    }
  }

  
  ngOnInit(): void {
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
