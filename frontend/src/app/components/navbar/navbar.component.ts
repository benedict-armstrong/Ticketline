import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { CartItemService } from 'src/app/services/cartItem.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {

  constructor(private authService: AuthService, private cartItemService: CartItemService) {}

  ngOnInit(): void {}

  toggleCart() {
    this.cartItemService.toggleStatus();
  }

  /**
   * Logout current user.
   */
  logoutUser(): void {
    this.cartItemService.closeCart();
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

  hasOrganizerPermission(): boolean {
    return this.authService.getUserRole() === 'ORGANIZER' || this.hasAdminPermission();
  }
}
