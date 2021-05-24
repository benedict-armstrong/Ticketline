import { HttpClient, HttpHeaders, HttpRequest } from '@angular/common/http';
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
                if (this.cart[i].id === cart[j].id || this.cart[i].ticketId === cart[j].ticketId) {
                  this.cart[i] = cart[j];
                  cart.splice(j, 1);
                }
              }           
            }
            for (let j = 0; j < cart.length; j++) {
              this.cart.push(cart[j]);            
            }
          }
          this.updatePrice();
        },
        (error) => {
          this.defaultServiceErrorHandling(error);
        }
      );
    }
    this.status = !this.status;
  }

  addToCart(item): void {
    this.postShoppingCart(item).subscribe(
      (cartItem: CartItem) => {
        this.success = true;
        for (let i = 0; i < this.cart.length; i++) {
          if (this.cart[i].id === cartItem.id || this.cart[i].ticketId === cartItem.ticketId) {
            this.cart[i] = cartItem;
            cartItem = null;
          }
        }
        if (cartItem != null) {
          this.cart.push(cartItem);
        }
        this.updatePrice();
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  removeFromCart(index): void {
    this.deleteShoppingCart(this.cart[index]).subscribe(
      (anwser) => {
        this.success = true;
        if (anwser === true) {
          this.cart.splice(index, 1);
          this.updatePrice();
        }
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  setAmount(index: number, amount: number): void {
    if (amount >= 0) {
      this.cart[index].amount = amount;
      this.postShoppingCart(this.cart[index]).subscribe(
        (cartItem: CartItem) => {
          this.success = true;
          for (let i = 0; i < this.cart.length; i++) {
            if (this.cart[i].id === cartItem.id || this.cart[i].ticketId === cartItem.ticketId) {
              this.cart[i] = cartItem;
              cartItem = null;
              break;
            }
          }
          if (cartItem != null) {
            this.cart.push(cartItem);
          }
          this.updatePrice();
        },
        (error) => {
          this.defaultServiceErrorHandling(error);
        }
      );
    }
  }

  getFromCart(index: number) {
    return this.cart[index];
  }

  updatePrice(): void {
    this.total = 0;
    this.cart.forEach(item => {
      this.total += this.price * item.amount;
    });
  }

  getShoppingCart(): Observable<CartItem[]> {
    return this.httpClient.get<CartItem[]>(this.userBaseUri + '/2');
  }

  postShoppingCart(cartItem: CartItem): Observable<CartItem> {
    return this.httpClient.post<CartItem>(this.userBaseUri, cartItem);
  }

  putShoppingCart(cartItem: CartItem): Observable<CartItem> {
    return this.httpClient.put<CartItem>(this.userBaseUri, cartItem);
  }

  deleteShoppingCart(cartItem: CartItem): Observable<Object> {
    return this.httpClient.request<Object>('delete', this.userBaseUri, { body: cartItem});
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
