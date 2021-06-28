import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TicketService } from 'src/app/services/ticket.service';

@Component({
  selector: 'app-shoppingcart',
  templateUrl: './shoppingcart.component.html',
  styleUrls: ['./shoppingcart.component.scss']
})
export class ShoppingcartComponent implements OnInit {

  public error = false;
  public success = false;
  public errorMessage = '';

  constructor(public ticketService: TicketService,
    private router: Router) {}

  ngOnInit(): void {
  }

  close(): void {
    this.success = false;
    this.ticketService.toggleStatus();
  }

  checkout(): void {
    this.ticketService.checkout().subscribe(
      (response) => {
        if (response) {
          this.ticketService.reload();
          this.router.navigate(['/user']);
        }
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  reserve(): void {
    this.ticketService.reserve().subscribe(
      (response) => {
        if (response) {
          this.ticketService.reload();
          this.router.navigate(['/user']);
          this.success = true;
        }
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  vanishAlert(): void {
    this.error = false;
    this.success = false;
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      if (error.error.error === '' || error.error.error === null || error.error.error === undefined) {
        this.errorMessage = 'The server did not respond.';
      } else {
        this.errorMessage = error.error.error;
      }
    } else {
      this.errorMessage = error.error;
    }
  }
}
