import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NewTicket } from 'src/app/dtos/newTicket';
import { Ticket } from 'src/app/dtos/ticket';
import { TicketService } from 'src/app/services/ticket.service';

@Component({
  selector: 'app-cart-item',
  templateUrl: './cart-item.component.html',
  styleUrls: ['./cart-item.component.scss']
})
export class CartItemComponent implements OnInit {
  @Input()
  i: number;

  cartItemForm: FormGroup;

  public success = false;
  public error = false;
  public errorMessage = '';
  public waiting = false;

  constructor(
    private formBuilder: FormBuilder,
    public ticketService: TicketService
  ) {
    this.cartItemForm = this.formBuilder.group({
      amount: [0, [Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.cartItemForm.setValue({amount: this.ticketService.cart.length});
  }

  removeAllTickets(): void {
    if (!this.waiting) {
      this.waiting = true;
      this.ticketService.removeMultipleTickets(this.ticketService.cart[this.i]).subscribe(
        (response: boolean) => {
          this.waiting = false;
          this.success = true;
          if (response) {
            this.ticketService.cart.splice(this.i, 1);
            this.ticketService.updatePrice();
          }
        },
        (error) => {
          this.waiting = false;
          this.ticketService.reload();
        }
      );
    }
  }

  removeTicket(j: number): void {
    if (!this.waiting) {
      this.waiting = true;
      this.ticketService.removeTicket(this.ticketService.cart[this.i][j]).subscribe(
        (response: boolean) => {
          this.waiting = false;
          this.success = true;
          if (response) {
            if (this.ticketService.cart[this.i].length === 1) {
              this.ticketService.cart.splice(this.i, 1);
            } else {
              this.ticketService.cart[this.i].splice(j, 1);
            }
            this.ticketService.updatePrice();
          }
        },
        (error) => {
          this.waiting = false;
          this.ticketService.reload();
        }
      );
    }
  }

  addTicketToCart(): void {
    if (!this.waiting) {
      this.waiting = true;
      this.vanishAlert();
      const addTicket: NewTicket = {
        performanceId: this.ticketService.cart[this.i][0].performance.id,
        ticketType: this.ticketService.cart[this.i][0].ticketType,
        amount: 1,
        seatId: null
      };
      this.ticketService.addTicket(addTicket).subscribe(
        (responseTickets: Ticket[]) => {
          this.waiting = false;
          this.success = true;
          if (responseTickets.length === 1) {
            this.ticketService.cart[this.i].push(responseTickets[0]);
          }
          this.ticketService.updatePrice();
        },
        (error) => {
          this.waiting = false;
          this.defaultServiceErrorHandling(error);
        }
      );
    }
  }

  vanishAlert(): void {
    this.error = false;
    this.success = false;
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
}
