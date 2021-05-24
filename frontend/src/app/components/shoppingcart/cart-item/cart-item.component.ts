import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ShoppingcartService } from 'src/app/services/shoppingcart.service';

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
    public cartService: ShoppingcartService
  ) {
    this.cartItemForm = this.formBuilder.group({
      amount: [0, [Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.cartItemForm.setValue({amount: this.cartService.cart[this.i].amount});
  }

  removeFromCart(): void {
    this.cartService.removeFromCart(this.i);
  }

  incAmount(): void {
    this.cartItemForm.setValue({amount: (this.cartItemForm.value.amount + 1)});
    this.cartService.setAmount(this.i, this.cartItemForm.value.amount);
  }

  decAmount(): void {
    if (this.cartItemForm.value.amount > 0) {
      this.cartItemForm.setValue({amount: this.cartItemForm.value.amount - 1});
      this.cartService.setAmount(this.i, this.cartItemForm.value.amount);
    }
  }

  setAmount(): void {
    if (this.cartItemForm.value.amount < 0) {
      this.cartItemForm.setValue({amount: 0});
    }
    this.cartService.setAmount(this.i, this.cartItemForm.value.amount);
  }
}
