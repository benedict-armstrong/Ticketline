import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/dtos/user';
import { AuthService } from 'src/app/services/auth.service';
import { ApplicationUserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.scss']
})
export class UserHomeComponent implements OnInit {

  // Error flag
  error = false;
  errorMessage = '';

  user: User;

  constructor(private applicationUserService: ApplicationUserService, private authService: AuthService, private router: Router) {
    if(this.router.getCurrentNavigation().extras.state){
      this.user = this.router.getCurrentNavigation().extras.state.user;
    }else{
      if(authService.isLoggedIn()){
        this.loadUser(authService.getUserEmail());
      }else{
        this.router.navigate(['']);
      }
    }
   }

  ngOnInit(): void {
  }



  /**
   * Load User with email from Backend.
   */
  loadUser(email: string){
    this.applicationUserService.getUserByEmail(email).subscribe(
      (response) => {
        this.user = response;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }


  vanishAlert(): void {
    this.error = false;
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
