import {Component, OnInit} from '@angular/core';
import {User} from '../../dtos/user';
import {UserService} from '../../services/user.service';
import {AuthService} from '../../services/auth.service';
import {NavigationStart, Router} from '@angular/router';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  managingUser: User;
  users: User[] = [];

  page = 0;
  size = 10;

  nothingToLoad = false;

  success = false;
  successMessage: string;

  error = null;

  constructor(private userService: UserService,
              private authService: AuthService,
              private router: Router) {
    router.events.subscribe((event: NavigationStart) => {
      if (event.navigationTrigger === 'popstate') {
        if (event.url === '/users') {
          this.reload();
        }
      }
    });
  }

  ngOnInit(): void {
    const email = this.authService.getUserEmail();
    this.userService.getUserByEmail(email).subscribe(
      user => {
        this.managingUser = user;
        this.loadBatch();
      }, error => {
        this.handleError(error);
      }
    );
  }

  loadBatch() {
    this.userService.getAll(this.page, this.size).subscribe(
      users => {
        this.users.push(...users);
        this.page++;
        if (users.length !== this.size) {
          this.nothingToLoad = true;
        }
      }, error => {
        this.handleError(error);
      }
    );
  }

  reload() {
    this.page = 0;
    this.users = [];
    this.loadBatch();
    this.nothingToLoad = false;
  }

  toggleStatus(user: User) {
    if (user.id === this.managingUser.id || user.role === 'ADMIN') {
      return;
    }
    if (user.status === 'ACTIVE') {
      user.status = 'BANNED';
    } else if (user.status === 'BANNED') {
      user.status = 'ACTIVE';
    }
    this.userService.updateUser(user).subscribe(
      response => {
        user = response;
      }, error => {
        this.handleError(error);
      }
    );
  }

  resetPassword(user: User) {
    this.userService.resetPassword(user.email).subscribe(
      response => {
        user = response;
        this.success = true;
        this.successMessage = 'The password for ' + user.email + ' has been reset.';
      }, error => {
        this.handleError(error);
      }
    );
  }

  handleError(error: Error) {
    this.error = error;
  }

  vanishAlert() {
    this.error = null;
    this.success = false;
  }

}
