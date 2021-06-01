import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CartItem } from 'src/app/dtos/cartItem';
import { TicketUpdate } from 'src/app/dtos/ticketUpdate';
import { CartItemService } from 'src/app/services/cartItem.service';

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
    public cartItemService: CartItemService
  ) {
    this.cartItemForm = this.formBuilder.group({
      amount: [0, [Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.cartItemForm.setValue({amount: this.cartItemService.cart[this.i].tickets.length});
  }

  removeFromCart(): void {
    if (!this.waiting) {
      this.removeFromCartHandler(this.i);
    }
  }

  removeTicketFromCart(j: number): void {
    if (!this.waiting) {
      if (this.cartItemService.cart[this.i].tickets.length > 1) {
        this.removeTicketFromCartHandler(this.i, j);
      } else {
        this.removeFromCartHandler(this.i);
      }
    }
  }

  addTicketToCart(): void {
    if (!this.waiting) {
      this.addTicketToCartHandler(this.i);
    }
  }

  addTicketToCartHandler(i: number) {
    this.waiting = true;
    this.vanishAlert();
    this.cartItemService.addTicketToCart(this.cartItemService.cart[i]).subscribe(
      (responseCartItem: CartItem) => {
        this.waiting = false;
        this.success = true;
        for (let j = 0; j < this.cartItemService.cart.length; j++) {
          if (this.cartItemService.cart[j].id === responseCartItem.id) {
            this.cartItemService.cart[j] = responseCartItem;
            responseCartItem = null;
            break;
          }
        }
        if (responseCartItem != null) {
          this.cartItemService.cart.push(responseCartItem);
        }
        this.cartItemService.updatePrice();
      },
      (error) => {
        this.waiting = false;
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  removeFromCartHandler(i: number) {
    this.waiting = true;
    this.cartItemService.removeCartItem(this.cartItemService.cart[i]).subscribe(
      (response: boolean) => {
        this.waiting = false;
        this.success = true;
        if (response) {
          this.cartItemService.cart.splice(i, 1);
          this.cartItemService.updatePrice();
        }
      },
      (error) => {
        this.waiting = false;
        this.cartItemService.reload();
      }
    );
  }

  removeTicketFromCartHandler(i: number, j: number) {
    this.waiting = true;
    this.cartItemService.removeTicketFromCart(this.cartItemService.cart[i].id, this.cartItemService.cart[i].tickets[j].id).subscribe(
      (response: boolean) => {
        this.waiting = false;
        this.success = true;
        if (response) {
          this.cartItemService.cart[i].tickets.splice(j, 1);
          this.cartItemService.updatePrice();
        }
      },
      (error) => {
        this.waiting = false;
        this.cartItemService.reload();
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
