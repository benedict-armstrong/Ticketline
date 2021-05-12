import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { ShoppingcartService } from 'src/app/services/shoppingcart.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  constructor(private authService: AuthService, private cartService: ShoppingcartService) {}

  ngOnInit(): void {
  }

  toggleCart() {
    this.cartService.status = !this.cartService.status;
  }

  /**
   * Logout current user.
   */
  logoutUser(): void {
    this.authService.logoutUser();
  }

  /**
   * Checks if user is logged in.
   */
  isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  hasAdminPermission(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }
}
