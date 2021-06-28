import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Globals } from '../global/globals';
import { Event } from '../dtos/event';
import { TopEvent } from '../dtos/topEvent';

@Injectable({
  providedIn: 'root'
})
export class ApplicationEventService {

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
   * Loads all topEvents from the backend with pagination
   */
   getTopEvents(page: number, size: number): Observable<TopEvent[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    return this.httpClient.get<TopEvent[]>(this.eventBaseUri + '/top', { params });
  }

  /**
   * Searches for all events in the backend with pagination
   */
  searchEvents(page: number, size: number, name: string, description: string, duration: number, eventType: string): Observable<Event[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    if (name !== '') {
      params = params.set('name', name);
    }
    if (description !== '') {
      params = params.set('description', description);
    }
    if (duration !== null) {
      params = params.set('duration', String(duration));
    }
    if (eventType !== '') {
      params = params.set('eventType', eventType);
    }

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
    if (event.performances.length > 1) {
      for (const p of event.performances) {
        if (this.hasOwnProperty(p.venue, 'id')) {
          const v = p.venue;
          event.performances.forEach((performance) => {
            // @ts-ignore
            if (this.hasOwnProperty(performance.venue, 'id') && performance.venue.id === v.id && performance.venue !== v) {
              // @ts-ignore
              performance.venue = v.id;
            }
          });
        }
      }
    }
    console.log(event);
    return this.httpClient.post<Event>(this.eventBaseUri, event);
  }

  /**
   * Searches for a text
   */
   fulltextSearchEvents(text: string, page: number, size: number): Observable<Event[]> {
    let params = new HttpParams();
    params = params.set('text', text);
    params = params.set('page', String(page));
    params = params.set('size', String(size));

    return this.httpClient.get<Event[]>(this.eventBaseUri + '/search', { params });
  }

  // eslint-disable-next-line @typescript-eslint/ban-types
  private hasOwnProperty<X extends {}, Y extends PropertyKey>(obj: X, prop: Y): obj is X & Record<Y, unknown> {
    return obj.hasOwnProperty(prop);
  }
}
