import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Booking} from '../dtos/booking';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private bookingBaseUri: string = this.globals.backendUri + '/bookings';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads bookings from the backend
   *
   */
  getBookings(): Observable<Booking[]> {
    //console.log('Loading bookings');
    return this.httpClient.get<Booking[]>(this.bookingBaseUri);
  }

}


