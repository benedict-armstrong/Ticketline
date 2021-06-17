import {Component, OnInit} from '@angular/core';
import {User} from '../../dtos/user';
import {UserService} from '../../services/user.service';
import {AuthService} from '../../services/auth.service';

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

  constructor(private userService: UserService,
              private authService: AuthService) { }

  ngOnInit(): void {
    const email = this.authService.getUserEmail();
    this.userService.getUserByEmail(email).subscribe(
      user => {
        this.managingUser = user;
      }, error => {
        alert(error);
      }
    );
    this.loadBatch();
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
        alert(error);
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
    if (user.id === this.managingUser.id) {
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
        alert(error);
      }
    );
  }

}
