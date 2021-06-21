import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { TicketService } from 'src/app/services/ticket.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {

  fulltextSearchForm: FormGroup;

  constructor(private authService: AuthService,
              private formBuilder: FormBuilder,
              private ticketService: TicketService,
              private router: Router) { }

  ngOnInit(): void {
    this.fulltextSearchForm = this.formBuilder.group({
      text: ['', []],
    });
  }

  toggleCart() {
    this.ticketService.toggleStatus();
  }

  /**
   * Logout current user.
   */
  logoutUser(): void {
    this.ticketService.closeCart();
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

  /**
   * Performs full text search over events
   */
  fullTextSearch() {
    this.router.navigate(['/events'], { queryParams: { search: this.fulltextSearchForm.value.text } });
  }
}
