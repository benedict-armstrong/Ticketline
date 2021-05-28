import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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
    this.ticketService.removeFromCart(this.i);
  }

  incAmount(): void {
    this.cartItemForm.setValue({amount: (this.cartItemForm.value.amount + 1)});
    this.ticketService.setAmount(this.i, this.cartItemForm.value.amount);
  }

  decAmount(): void {
    if (this.cartItemForm.value.amount > 0) {
      this.cartItemForm.setValue({amount: this.cartItemForm.value.amount - 1});
      this.ticketService.setAmount(this.i, this.cartItemForm.value.amount);
    }
  }

  setAmount(): void {
    if (this.cartItemForm.value.amount < 0) {
      this.cartItemForm.setValue({amount: 0});
    }
    this.ticketService.setAmount(this.i, this.cartItemForm.value.amount);
  }
}
