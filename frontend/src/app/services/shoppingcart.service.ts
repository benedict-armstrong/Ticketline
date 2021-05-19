import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ShoppingcartService {

  cart = [];
  status = false;

  constructor() { }

  addToCart(item): void {
    this.cart.push(item);
  }

  removeFromCart(index): void {
    this.cart.splice(index, 1);
  }

  incAmount(index): void {
    this.cart[index].amount += 1;
  }

  decAmount(index): void {
    this.cart[index].amount -= 1;
    console.log(this.cart);
  }
}
