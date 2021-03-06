import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global/globals';
import {Observable, Subject} from 'rxjs';
import { NewTicket } from '../dtos/newTicket';
import { Ticket } from '../dtos/ticket';
import { CustomFile } from '../dtos/customFile';
import { SeatCount } from '../dtos/seatCount';
import {LayoutUnit} from '../dtos/layoutUnit';
import {Performance} from '../dtos/performance';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  public cartSubject = new Subject<Ticket[][]>();
  cartState$ = this.cartSubject.asObservable();

  public cart: Ticket[][] = [];
  public showCart = false;
  public waiting = false;
  public success = false;
  public error = false;
  public loading = false;
  public errorMessage = '';
  public total = 0;
  public prices: number[];
  public sameSectors: boolean[];

  private ticketBaseUri: string = this.globals.backendUri + '/tickets';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  updateCartState(): void {
    this.cartSubject.next( this.cart );
  }

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

  updateWrapper(): void {
    this.cart.sort((a, b) => a[0].id - b[0].id);

    for (const cartItem of this.cart) {
      cartItem.sort((a, b) => a.seat.id - b.seat.id);
    }
    this.updatePrice();
    this.updateSameSector();
    this.updateCartState();
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

  updateSameSector(): void {
    this.sameSectors = [];
    this.cart.forEach(inner => {
      let sectorId = null;
      let done = false;
      for (const ticket of inner) {
        if (sectorId === null) {
          sectorId = ticket.ticketType.sector.id;
        } else {
          if (sectorId !== ticket.ticketType.sector.id) {
            this.sameSectors.push(false);
            done = true;
            break;
          }
        }
      }
      this.sameSectors.push(true);
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
          this.updateWrapper();

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
          this.updateWrapper();
          subscriber.next(this.cart);
        },
        (error) => {
          subscriber.error(error);
        }
      );
    });
  }

  updateTicketType(ticket: Ticket): Observable<Ticket> {
    return new Observable<Ticket>(subscriber => {
      this.httpClient.put<Ticket>(this.ticketBaseUri + '/update', ticket).subscribe(
        (response: Ticket) => {
          if (response) {

            const oldTicket = this.cart.find(
              (tickets) => tickets[0].performance.id === ticket.performance.id)
              .find((t) => t.id === ticket.id);
            oldTicket.ticketType = ticket.ticketType;

            this.updateWrapper();
            subscriber.next(ticket);
          }
        },
        (error) => {
          this.reload();
          subscriber.error(error);
        }
      );
    });
  }

  removeTicketBySeatAndPerformance(seat: LayoutUnit, performance: Performance): Observable<Ticket[][]> {
    const ticketToRemove = [].concat(...this.cart).filter(
      (t: Ticket) => t.performance.id === performance.id && t.seat.id === seat.id)[0] as Ticket;
    return new Observable<Ticket[][]>(subscriber => {
      this.httpClient.delete<boolean>(this.ticketBaseUri + '/' + ticketToRemove.id).subscribe(
        (response: boolean) => {
          if (response) {
            this.cart.forEach((tickets) => {
              if (tickets.find((t) => t === ticketToRemove)) {
                if (tickets.length === 1) {
                  this.cart.splice( this.cart.indexOf(tickets), 1);
                } else {
                  tickets.splice(tickets.indexOf(ticketToRemove), 1);
                }
              }
            });
            this.updateWrapper();
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
            this.updateWrapper();
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
            this.updateWrapper();
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

  getSales(): Observable<number[]> {
    return this.httpClient.get<number[]>(this.ticketBaseUri + '/sales');
  }

  private defaultServiceErrorHandling(error: any) {
    //console.log(error);
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
