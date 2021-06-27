import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global/globals';
import { Observable } from 'rxjs';
import { NewTicket } from '../dtos/newTicket';
import { Ticket } from '../dtos/ticket';
import { CustomFile } from '../dtos/customFile';
import { SeatCount } from '../dtos/seatCount';
import {LayoutUnit} from '../dtos/layoutUnit';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  public cart: Ticket[][] = [];
  public showCart = false;
  public waiting = false;
  public success = false;
  public error = false;
  public loading = false;
  public errorMessage = '';
  public total = 0;
  public prices: number[];

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
    this.showCart = true;
    this.reload();
  }

  reload(): void {
    this.updateShoppingCart().subscribe(() => {
    },
    (error) => {
      this.defaultServiceErrorHandling(error);
  });
  }

  updatePrice(): void {
    this.total = 0;
    this.prices = [];
    this.cart.forEach(inner => {
      let change = 0;
      inner.forEach(ticket => {
        change += ticket.ticketType.price;
      });
      this.prices.push(change);
      this.total += change;
    });
  }

  updateShoppingCart(): Observable<boolean> {
    return new Observable<boolean>(subscriber => {
      this.error = false;
      this.loading = true;

      this.httpClient.get<Ticket[]>(this.ticketBaseUri).subscribe(
        (responseList: Ticket[]) => {
          const newCart: Ticket[][] = [];
          responseList.forEach(ticket => {
            if (newCart.length === 0) {
              newCart.push([ticket]);
            } else {
              let done = false;
              for (const newCartInner of newCart) {
                if (newCartInner.length === 0) {
                  newCartInner.push(ticket);
                  done = true;
                  break;
                } else {
                  if (newCartInner[0].performance.id === ticket.performance.id) {
                    newCartInner.push(ticket);
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
          this.updatePrice();

          this.loading = false;
          this.success = true;

          subscriber.next();
        },
        (error) => {
          this.loading = false;
          subscriber.error(error);
        }
        );
    });
  }

  getSeatCounts(performanceId: number): Observable<SeatCount[]> {
    return this.httpClient.get<SeatCount[]>(this.ticketBaseUri + '/' + performanceId + '/seatCounts');
  }

  getPaidItems(): Observable<Ticket[]> {
    return this.httpClient.get<Ticket[]>(this.ticketBaseUri + '/paid');
  }

  getReservedItems(): Observable<Ticket[]> {
    return this.httpClient.get<Ticket[]>(this.ticketBaseUri + '/reserved');
  }

  addTicket(addTicket: NewTicket): Observable<Ticket[][]> {
    return new Observable<Ticket[][]>(subscriber => {
      this.httpClient.post<Ticket[]>(this.ticketBaseUri, addTicket).subscribe(
        (tickets: Ticket[]) => {
          let done = false;

          for (let i = 0; i < this.cart.length; i++) {
            if (this.cart[i].length === 0) {
              this.cart[i] = tickets;
              done = true;
              break;
            } else {
              if (this.cart[i][0].performance.id === tickets[0].performance.id) {
                tickets.forEach(ticket => {
                  this.cart[i].push(ticket);
                });
                done = true;
                break;
              }
            }
          }

          if (!done) {
            this.cart.push(tickets);
          }
          this.updatePrice();
          subscriber.next(this.cart);
        },
        (error) => {
          subscriber.error(error);
        }
      );
    });
  }

  removeTicketBySeat(seat: LayoutUnit): Observable<Ticket[][]> {
    const ticket: Ticket = [].concat(...this.cart).find((t: Ticket) => t.seat.id === seat.id );
    return new Observable<Ticket[][]>(subscriber => {
      this.httpClient.delete<boolean>(this.ticketBaseUri + '/' + ticket.id).subscribe(
        (response: boolean) => {
          if (response) {
            this.cart.forEach((tickets) => {
              if (tickets.find((t) => t === ticket)) {
                if (tickets.length === 1) {
                  this.cart.splice( this.cart.indexOf(tickets), 1);
                } else {
                  tickets.splice(tickets.indexOf(ticket), 1);
                }
              }
            });
            this.updatePrice();
            subscriber.next(this.cart);
          }
        },
        (error) => {
          this.reload();
          subscriber.error(error);
        }
      );
    });
  }

  removeTicket(cartIndex: number, ticketIndex: number): Observable<Ticket[][]> {
    const ticket: Ticket = this.cart[cartIndex][ticketIndex];
    return new Observable<Ticket[][]>(subscriber => {
      this.httpClient.delete<boolean>(this.ticketBaseUri + '/' + ticket.id).subscribe(
        (response: boolean) => {
          if (response) {
            if (this.cart[cartIndex].length === 1) {
              this.cart.splice(cartIndex, 1);
            } else {
              this.cart[cartIndex].splice(ticketIndex, 1);
            }
            this.updatePrice();
            subscriber.next(this.cart);
          }
        },
        (error) => {
          this.reload();
          subscriber.error(error);
        }
      );
    });
  }

  removeMultipleTickets(cartIndex: number): Observable<boolean> {
    return new Observable<boolean>(subscriber => {
      const ids: number[] = [];
      this.cart[cartIndex].forEach(ticket => {
        ids.push(ticket.id);
      });

      this.httpClient.request<boolean>('DELETE', this.ticketBaseUri, { body: ids }).subscribe(
        (response: boolean) => {
          if (response) {
            this.cart.splice(cartIndex, 1);
            this.updatePrice();
            subscriber.next();
          }
        },
        (error) => {
          this.reload();
          subscriber.error(error);
        }
      );
    });
  }

  checkout(): Observable<boolean> {
    return this.httpClient.put<boolean>(this.ticketBaseUri + '/checkout', null);
  }

  reserve(): Observable<boolean> {
    return this.httpClient.put<boolean>(this.ticketBaseUri + '/reserve', null);
  }

  getTicketPdf(id: number): Observable<CustomFile> {
    return this.httpClient.get<CustomFile>(this.ticketBaseUri + '/pdf/' + id);
  }

  confirmation(userId: number, performanceId: number): Observable<Ticket[]> {
    return this.httpClient.get<Ticket[]>(this.ticketBaseUri + '/confirmation?user=' + userId + '&performance=' + performanceId);
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
