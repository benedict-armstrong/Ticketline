import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global/globals';
import { Observable } from 'rxjs';
import { NewTicket } from '../dtos/newTicket';
import { Ticket } from '../dtos/ticket';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  public cart = [];
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
      (responseList: Ticket[]) => {
        this.loading = false;
        this.success = true;

        let newCart: Ticket[][] = [];
        responseList.forEach(ticket => {
          if (newCart.length == 0) {
            newCart.push([ticket]);
          } else {
            let done = false;
            for (let i = 0; i < newCart.length; i++) {
              if (newCart[i].length == 0) {
                newCart[i].push(ticket);
                done = true;
                break;
              } else {
                if (newCart[i][0].performance.id == ticket.performance.id) {
                  newCart[i].push(ticket);
                  done = true;
                  break;
                }
              }
            }
            if (!done) {
              newCart.push([ticket]);
            }
          }
        });

        this.cart = newCart;
        console.log(this.cart);
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
    this.cart.forEach(inner => {
      inner.forEach(ticket => {
        this.total += ticket.ticketType.price;
      });
    });
  }

  getShoppingCart(): Observable<Ticket[]> {
    return this.httpClient.get<Ticket[]>(this.ticketBaseUri);
  }

  getPaidItems(): Observable<Ticket[]> {
    return this.httpClient.get<Ticket[]>(this.ticketBaseUri + '/paid');
  }

  addTicket(addTicket: NewTicket, amount: number): Observable<Ticket[]> {
    return this.httpClient.post<Ticket[]>(this.ticketBaseUri + '/' + amount, addTicket);
  }

  removeTicket(ticket: Ticket): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.ticketBaseUri + '/' + ticket.id);
  }

  checkout(): Observable<boolean> {
    return this.httpClient.put<boolean>(this.ticketBaseUri + '/checkout', null);
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
