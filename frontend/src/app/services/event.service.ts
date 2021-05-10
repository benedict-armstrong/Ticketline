import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Event} from '../dtos/event';

@Injectable({
  providedIn: 'root'
})
export class ApplicationEventService {

  private eventBaseUri: string = this.globals.backendUri + '/events';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all events from the backend
   */
  getEvents(): Observable<Event[]> {
    return this.httpClient.get<Event[]>(this.eventBaseUri);
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
