import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthRequest } from 'src/app/dtos/auth-request';
import { AuthService } from 'src/app/services/auth.service';
import { User } from '../../dtos/user';

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

  constructor(private formBuilder: FormBuilder, private router: Router) {
    this.addUserForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required, Validators.minLength(8)]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      points: [0, [Validators.min(0)]]
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
   addUser() {
    this.submitted = true;
    if (this.addUserForm.valid) {
      var user:User = new User(null
        , this.addUserForm.value.email
        , this.addUserForm.value.firstName
        , this.addUserForm.value.lastName
        , this.addUserForm.value.points, null); 
      const authRequest: AuthRequest = new AuthRequest(this.addUserForm.controls.username.value, this.addUserForm.controls.password.value);
    } else {
      console.log('Invalid input');
    }
  }

  
  ngOnInit(): void {
  }
}
