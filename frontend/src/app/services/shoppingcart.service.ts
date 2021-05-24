import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ShoppingcartService {
  cart = [];
  status = false;
  total = 0;

  constructor() { }

  addToCart(item): void {
    this.cart.push(item);
    this.updatePrice();
  }

  removeFromCart(index): void {
    this.cart.splice(index, 1);
    this.updatePrice();
  }

  setAmount(index: number, amount: number): void {
    if (amount >= 0) {
      this.cart[index].amount = amount;
    }
    this.updatePrice();
  }

  getFromCart(index: number) {
    return this.cart[index];
  }

  updatePrice(): void {
    this.total = 0;
    this.cart.forEach(item => {
      console.log(item.amount);
      this.total += item.price * item.amount;
    });
  }
}
