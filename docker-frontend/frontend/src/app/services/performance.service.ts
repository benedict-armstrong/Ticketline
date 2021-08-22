import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Performance} from '../dtos/performance';

@Injectable({
  providedIn: 'root'
})
export class ApplicationPerformanceService {

  private performanceBaseUri: string = this.globals.backendUri + '/performances';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all events from the backend with pagination
   */
  getEvents(page: number, size: number): Observable<Performance[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    return this.httpClient.get<Performance[]>(this.performanceBaseUri, { params });
  }

  /**
   * Loads a specific performance from the backend
   */
  getPerformanceById(id: number): Observable<Performance> {
    return this.httpClient.get<Performance>(this.performanceBaseUri + '/' + id);
  }

  /**
   * Add a new performance
   */
  addPerformance(performance: Performance): Observable<Performance> {
    return this.httpClient.post<Performance>(this.performanceBaseUri, performance);
  }

  /**
   * Search all Performances
   */
  searchPerformances(page: number, size: number, date: Date, event: number, venue: number): Observable<Performance[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    if (date) {
      params = params.set('date', date.toISOString());
    }
    if (event !== null) {
      params = params.set('eventId', event.toString());
    }

    if (venue !== null) {
      params = params.set('venue', venue.toString());
    }

    return this.httpClient.get<Performance[]>(this.performanceBaseUri, { params });
  }

  /**
   * Get all Performances
   */
  getPerformances(page: number, size: number): Observable<Performance[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));

    return this.httpClient.get<Performance[]>(this.performanceBaseUri, { params });
  }

  /**
   * Loads all events for one artist from the backend with pagination
   */
  getPerformancesForArtistId(id: number, page: number, size: number): Observable<Performance[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    params = params.set('artistId', String(id));
    return this.httpClient.get<Performance[]>(this.performanceBaseUri, { params });
  }

  /**
   * Loads all events for one address from the backend with pagination
   */
  getPerformancesForAddressId(id: number, page: number, size: number): Observable<Performance[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    params = params.set('addressId', String(id));
    return this.httpClient.get<Performance[]>(this.performanceBaseUri, { params });
  }
}
