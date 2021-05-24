import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CartItem } from '../dtos/cartItem';
import { Globals } from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class ShoppingcartService {
  private userBaseUri: string = this.globals.backendUri + '/cart';

  public cart: CartItem[] = [];
  public status = false;
  public total = 0;
  public success = false;
  public error = false;
  public errorMessage = "";

  public price = 10;

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  toggleStatus(): void {
    if (!this.status) {
      this.getShoppingCart().subscribe(
        (cart: CartItem[]) => {
          this.success = true;
          if (cart.length > 0) {
            for (let i = 0; i < this.cart.length; i++) {
              for (let j = 0; j < cart.length; j++) {
                console.log("Check");
                if (this.cart[i].id === cart[j].id) {
                  this.cart[i] = cart[j];
                  cart.splice(j, 1);
                }              
              }           
            }
            for (let j = 0; j < cart.length; j++) {
              this.cart.push(cart[j]);            
            }
          }
          console.log(cart);
        },
        (error) => {
          this.defaultServiceErrorHandling(error);
        }
      );
    }
    this.status = !this.status;
  }

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
      this.total += this.price * item.amount;
    });
  }

  getShoppingCart(): Observable<CartItem[]> {
    return this.httpClient.get<CartItem[]>(this.userBaseUri + '/2');
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
