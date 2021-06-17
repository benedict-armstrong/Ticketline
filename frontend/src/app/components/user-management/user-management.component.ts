import { Component, OnInit } from '@angular/core';
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
  users: User[];

  page = 0;
  size = 10;

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
    this.userService.getAll(this.page, this.size).subscribe(
      users => {
        this.users = users;
      }, error => {
        alert(error);
      }
    );
  }

}
