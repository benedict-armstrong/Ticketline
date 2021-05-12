import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  
  @Input() cartToggle: boolean;
  @Output() toggleEvent = new EventEmitter<boolean>();

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
  }

  toggleCart() {
    this.cartToggle = !this.cartToggle;
    this.toggleEvent.emit(this.cartToggle);
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
