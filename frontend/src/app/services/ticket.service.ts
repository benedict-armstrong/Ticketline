import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Ticket} from '../dtos/ticket';
import {TicketUpdate} from '../dtos/ticketUpdate';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  public cart: Ticket[] = [];
  public showCart = false;
  public waiting = false;
  public success = false;
  public error = false;
  public errorMessage = '';
  public total = 0;

  private ticketBaseUri: string = this.globals.backendUri + '/tickets';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  toggleStatus(): void {
    if (!this.showCart) {
      this.getShoppingCart().subscribe(
        (responseList: Ticket[]) => {
          this.success = true;
          this.cart = responseList;
          this.updatePrice();
        },
        (error) => {
          this.defaultServiceErrorHandling(error);
        }
      );
    }
    this.showCart = !this.showCart;
  }

  updatePrice(): void {
    this.total = 0;
    this.cart.forEach(item => {
      this.total += item.ticketType.price * item.seats[0];
    });
  }

  addToCart(ticket: Ticket): void {
    this.addTicket(ticket).subscribe(
      (responseTicket: Ticket) => {
        this.success = true;
        for (let i = 0; i < this.cart.length; i++) {
          if (this.cart[i].id === responseTicket.id || this.cart[i].ticketType.id === responseTicket.ticketType.id) {
            this.cart[i] = responseTicket;
            responseTicket = null;
            break;
          }
        }
        if (responseTicket != null) {
          this.cart.push(responseTicket);
        }
        this.updatePrice();
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  setAmount(i: number, amount: number) {
    this.updateAmount({id: this.cart[i].id, seats: [amount]}).subscribe(
      (responseTicket: TicketUpdate) => {
        this.success = true;
        this.cart[i].seats = responseTicket.seats;
        this.updatePrice();
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  removeFromCart(i: number) {
    this.removeTicket(this.cart[i]).subscribe(
      (response: boolean) => {
        if (response) {
          this.cart.splice(i, 1);
          this.updatePrice();
        }
      },
      (error) => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  getShoppingCart(): Observable<Ticket[]> {
    return this.httpClient.get<Ticket[]>(this.ticketBaseUri + '/cart');
  }

  addTicket(ticket: Ticket): Observable<Ticket> {
    return this.httpClient.post<Ticket>(this.ticketBaseUri + '/cart', ticket);
  }

  updateAmount(ticketUpdate: TicketUpdate): Observable<TicketUpdate> {
    return this.httpClient.put<TicketUpdate>(this.ticketBaseUri + '/amount', ticketUpdate);
  }

  removeTicket(ticket: Ticket): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.ticketBaseUri + '/' + ticket.id);
  }

  /*
  buy(ticket: Ticket): Observable<Ticket> {
    let params = new HttpParams();
    params = params.set('mode', 'buy');
    return this.httpClient.post<Ticket>(this.ticketBaseUri, ticket, {params});
  }
  */

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
