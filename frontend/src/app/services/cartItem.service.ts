import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global/globals';
import { Observable } from 'rxjs';
import { TicketUpdate } from '../dtos/ticketUpdate';
import { CartItem } from '../dtos/cartItem';

@Injectable({
  providedIn: 'root'
})
export class CartItemService {
  public cart: CartItem[] = [];
  public showCart = false;
  public waiting = false;
  public success = false;
  public error = false;
  public loading = false;
  public errorMessage = '';
  public total = 0;

  private cartItemBaseUri: string = this.globals.backendUri + '/cartItems';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  toggleStatus(): void {
    if (this.showCart) {
      this.closeCart();
    } else {
      this.openCart();
    }
  }

  closeCart(): void {
    this.showCart = false;
  }

  openCart(): void {
    this.reload();
    this.showCart = true;
  }

  reload(): void {
    this.error = false;
    this.loading = true;
    this.getShoppingCart().subscribe(
      (responseList: CartItem[]) => {
        console.log(responseList);
        this.loading = false;
        this.success = true;
        this.cart = responseList;
        this.updatePrice();
      },
      (error) => {
        this.loading = false;
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  updatePrice(): void {
    this.total = 0;
    this.cart.forEach(item => {
      item.tickets.forEach(ticket => {
        this.total += ticket.ticketType.price;
      });
    });
  }

  getShoppingCart(): Observable<CartItem[]> {
    return this.httpClient.get<CartItem[]>(this.cartItemBaseUri);
  }

  getPaidItems(): Observable<CartItem[]> {
    return this.httpClient.get<CartItem[]>(this.cartItemBaseUri + '/paid');
  }

  addCartItem(cartItem: CartItem, amount: number): Observable<CartItem> {
    return this.httpClient.post<CartItem>(this.cartItemBaseUri + '/' + amount, cartItem);
  }

  removeCartItem(cartItem: CartItem): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.cartItemBaseUri + '/' + cartItem.id);
  }

  checkout(): Observable<boolean> {
    return this.httpClient.put<boolean>(this.cartItemBaseUri + '/checkout', null);
  }

  addTicketToCart(cartItem: CartItem): Observable<CartItem> {
    return this.httpClient.post<CartItem>(this.cartItemBaseUri + '/' + cartItem.id + '/addTicket', null);
  }

  removeTicketFromCart(cartItemId: number, ticketId: number): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.cartItemBaseUri + '/' + cartItemId + '/' + ticketId);
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      if (error.error.error === '' || error.error.error === null || error.error.error === undefined) {
        this.errorMessage = 'The server did not respond.';
      } else {
        this.errorMessage = error.error.error;
      }
    } else {
      this.errorMessage = error.error;
    }
  }

}
