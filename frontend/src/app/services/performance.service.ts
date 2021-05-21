import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Performance} from '../dtos/performance';

@Injectable({
  providedIn: 'root'
})
export class ApplicationPerformanceService {

  private eventBaseUri: string = this.globals.backendUri + '/performances';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all events from the backend with pagination
   */
  getEvents(page: number, size: number): Observable<Performance[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    return this.httpClient.get<Performance[]>(this.eventBaseUri, { params });
  }

  /**
   * Loads all events from the backend
   */
  getEventById(id: number): Observable<Performance> {
    return this.httpClient.get<Performance>(this.eventBaseUri + '/' + id);
  }

  /**
   * Add a new event
   */
  addEvent(event: Performance): Observable<Performance> {
    console.log(event);
    return this.httpClient.post<Performance>(this.eventBaseUri, event);
  }
}
