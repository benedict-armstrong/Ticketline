import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TicketUpdate } from 'src/app/dtos/ticketUpdate';
import { TicketService } from 'src/app/services/cartItem.service';

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
    this.cartItemForm.setValue({amount: this.ticketService.cart[this.i].seats[0]});
  }

  removeFromCart(): void {
    if (!this.waiting) {
      this.removeFromCartHandler(this.i);
    }
  }

  incAmount(): void {
    if (!this.waiting) {
      this.updateAmount(this.i, this.cartItemForm.value.amount + 1);
    }
  }

  decAmount(): void {
    if (!this.waiting) {
      if (this.cartItemForm.value.amount > 0) {
        this.updateAmount(this.i, this.cartItemForm.value.amount - 1);
      }
    }
  }

  setAmount(): void {
    if (!this.waiting) {
      if (this.cartItemForm.value.amount < 0) {
        this.cartItemForm.setValue({amount: 0});
      }
      this.updateAmount(this.i, this.cartItemForm.value.amount);
    }
  }

  updateAmount(i: number, amount: number) {
    this.waiting = true;
    this.vanishAlert();
    const oldAmount = this.ticketService.cart[i].seats[0];
    this.ticketService.updateAmount({id: this.ticketService.cart[i].id, seats: [amount]}).subscribe(
      (responseTicket: TicketUpdate) => {
        this.waiting = false;
        this.success = true;
        this.ticketService.cart[i].seats = responseTicket.seats;
        this.ticketService.updatePrice();
        this.cartItemForm.setValue({amount: this.ticketService.cart[i].seats[0]});
      },
      (error) => {
        this.waiting = false;
        this.cartItemForm.setValue({amount: oldAmount});
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  removeFromCartHandler(i: number) {
    this.waiting = true;
    this.ticketService.removeTicket(this.ticketService.cart[i]).subscribe(
      (response: boolean) => {
        this.waiting = false;
        this.success = true;
        if (response) {
          this.ticketService.cart.splice(i, 1);
          this.ticketService.updatePrice();
        }
      },
      (error) => {
        this.waiting = false;
        this.ticketService.reload();
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
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }
}
