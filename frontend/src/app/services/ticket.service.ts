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
  public loading = false;
  public errorMessage = '';
  public total = 0;

  private ticketBaseUri: string = this.globals.backendUri + '/tickets';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  toggleStatus(): void {
    if (!this.showCart) {
      this.loading = true;
      this.getShoppingCart().subscribe(
        (responseList: Ticket[]) => {
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
    this.showCart = !this.showCart;
  }

  reload(): void {
    this.error = false;
    this.loading = true;
    this.getShoppingCart().subscribe(
      (responseList: Ticket[]) => {
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
      this.total += item.ticketType.price * item.seats[0];
    });
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
