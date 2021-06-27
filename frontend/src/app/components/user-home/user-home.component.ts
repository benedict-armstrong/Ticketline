import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/dtos/user';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';
import {TicketService} from '../../services/ticket.service';
import {Ticket} from '../../dtos/ticket';
import {TicketGroup} from '../../dtos/ticketGroup';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.scss'],
})
export class UserHomeComponent implements OnInit {

  @ViewChild('deletionModal') deletionModal: ElementRef;

  // Error flag
  error = false;
  errorMessage = '';
  passwordError = false;
  passwordErrorMessage = '';
  // Success Flag
  passwordSuccess = false;

  // Show User Actions
  userActions = false;
  loading = false;
  showAll = false;
  notPaid = false;

  // If password change is submitted
  passwordSubmitted = false;

  user: User;
  userRole: string;
  tickets: Ticket[] = [];
  orders: TicketGroup[] = [];
  newOrders: TicketGroup[] = [];
  passwordChangeForm: FormGroup;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private ticketService: TicketService,
    private router: Router,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.loadUser(this.authService.getUserEmail());
    } else {
      this.router.navigate(['']);
    }

    this.passwordChangeForm = this.formBuilder.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
      passwordRepeat: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  /**
   * Load User with email from Backend.
   */
  loadUser(email: string) {
    this.userService.getUserByEmail(email).subscribe(
      (response) => {
        this.user = response;
        this.loading = true;
        this.loadOrders();
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Gets all paid tickets from Backend and sorts by performance.
   */
  loadOrders() {
    this.ticketService.getPaidItems().subscribe(
      (response) => {
        // Get all tickets
        response.forEach(value => {
          this.createTicketGroups(value, false, false);
        });

        this.loadReserved();
        this.loadCancelled();
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  loadReserved() {
    console.log('loading reserved orders');
    this.ticketService.getReservedItems().subscribe(
      (response) => {
        // Get all reserved tickets
        if (response.length > 0) {
          this.notPaid = true;
        }
        response.forEach(value => {
          this.createTicketGroups(value, true, false);
        });

        this.orders.sort((x, y) =>
          Date.parse(x.tickets[0].performance.date) - Date.parse(y.tickets[0].performance.date)).reverse();
        this.newOrders = this.orders.filter(item => item.old === false && item.cancelled === false);
        this.loading = false;
        console.log(this.orders);
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  loadCancelled() {
    console.log('loading cancelled orders');
    this.ticketService.getCancelledItems().subscribe(
      (response) => {
        response.forEach(value => {
          this.createTicketGroups(value, false, true);
        });

        this.orders.sort((x, y) =>
          Date.parse(x.tickets[0].performance.date) - Date.parse(y.tickets[0].performance.date)).reverse();
        this.loading = false;
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Sorting ticket into right TicketGroup
   */
  createTicketGroups(ticket, reserved, cancelled) {
    const ticketGroup = this.orders.find(i => i.id === ticket.performance.id && i.reserved === reserved && i.cancelled === cancelled);
    if (ticketGroup == null) {
      const date = Date.parse(ticket.performance.date);
      const old = date < Date.now();
      const newGroup = new TicketGroup(ticket.performance.id, [ticket], old, reserved, cancelled);
      this.orders.push(newGroup);
    } else {
      ticketGroup.tickets.push(ticket);
    }
  }

  /**
   * Password change
   */
  changePassword() {
    this.passwordSubmitted = true;
    if (this.passwordChangeForm.valid) {
      if (
        this.passwordChangeForm.value.password ===
        this.passwordChangeForm.value.passwordRepeat
      ) {
        this.user.password = this.passwordChangeForm.value.password;

        this.userService.updateUser(this.user).subscribe(
          () => {
            this.passwordSubmitted = false;
            this.passwordChangeForm.reset();
            this.passwordSuccess = true;
          },
          (error) => {
            this.defaultPasswordErrorHandling(error);
          }
        );
      }
    }
  }

  deleteAccount() {
    this.userService.delete(this.user.id).subscribe(
      response => {
        this.deletionModal.nativeElement.click(); // close modal
        this.authService.logoutUser();
        this.router.navigate(['/deleted']);
      }, error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  vanishAlert(): void {
    this.error = false;
    this.passwordSuccess = false;
    this.passwordError = false;
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

  private defaultPasswordErrorHandling(error: any) {
    console.log(error);
    this.passwordError = true;
    if (typeof error.error === 'object') {
      this.passwordErrorMessage = error.error.error;
    } else {
      this.passwordErrorMessage = error.error;
    }
  }
}
