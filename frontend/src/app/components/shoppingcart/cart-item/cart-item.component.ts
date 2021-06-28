import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NewTicket } from 'src/app/dtos/newTicket';
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
      this.ticketService.removeMultipleTickets(this.i).subscribe(
        () => {
          this.waiting = false;
          this.success = true;
        },
        (error) => {
          //console.error(error);
          this.waiting = false;
        }
      );
    }
  }

  removeTicket(j: number): void {
    if (!this.waiting) {
      this.waiting = true;
      this.ticketService.removeTicket(this.i, j).subscribe(
        () => {
          this.waiting = false;
          this.success = true;
        },
        (error) => {
          //console.error(error);
          this.waiting = false;
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
        () => {
          this.waiting = false;
          this.success = true;
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
    //console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }
}
