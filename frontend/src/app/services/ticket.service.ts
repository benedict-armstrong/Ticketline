import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Ticket} from '../dtos/ticket';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  private newsBaseUri: string = this.globals.backendUri + '/tickets';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  buy(ticket: Ticket): Observable<Ticket> {
    let params = new HttpParams();
    params = params.set('mode', 'buy');
    return this.httpClient.post<Ticket>(this.newsBaseUri, ticket, {params});
  }

}
