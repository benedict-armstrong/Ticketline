import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Event} from '../dtos/event';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private eventBaseUri: string = this.globals.backendUri + '/events';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all events from the backend with pagination
   */
  getEvents(page: number, size: number): Observable<Event[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    return this.httpClient.get<Event[]>(this.eventBaseUri, { params });
  }

  /**
   * Loads all events from the backend
   */
  getEventById(id: number): Observable<Event> {
    return this.httpClient.get<Event>(this.eventBaseUri + '/' + id);
  }

  /**
   * Add a new event
   */
  addEvent(event: Event): Observable<Event> {
    console.log(event);
    return this.httpClient.post<Event>(this.eventBaseUri, event);
  }
}
