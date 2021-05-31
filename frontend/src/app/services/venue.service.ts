import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Venue } from '../dtos/venue';
import { Globals } from '../global/globals';

@Injectable({
  providedIn: 'root',
})
export class VenueService {
  private userBaseUri: string = this.globals.backendUri + '/venues';

  constructor(private globals: Globals, private httpClient: HttpClient) {}

  create(venue: Venue): Observable<Venue> {
    console.log('Save Venue');
    return this.httpClient.post<Venue>(this.userBaseUri, venue);
  }
}
