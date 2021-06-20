import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {UserService} from '../../services/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-banned-alert',
  templateUrl: './banned-alert.component.html',
  styleUrls: ['./banned-alert.component.scss']
})
export class BannedAlertComponent implements OnInit {

  constructor(private authService: AuthService,
              private userService: UserService,
              private router: Router) { }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
    }
    this.userService.getUserByEmail(this.authService.getUserEmail()).subscribe(
      response => {
        if (response.status !== 'BANNED') {
          this.router.navigate(['/']);
        }
      }, error => {
        console.error(error);
      }
    );
  }

}
