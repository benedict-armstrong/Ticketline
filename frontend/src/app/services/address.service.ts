import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Address} from '../dtos/address';

@Injectable({
  providedIn: 'root'
})
export class AddressService {
  private addressBaseUri: string = this.globals.backendUri + '/addresses';

  constructor(private http: HttpClient, private globals: Globals) { }

  getAddresses(page: number, size: number): Observable<Address[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    return this.http.get<Address[]>(this.addressBaseUri, {params});
  }

  getAddressById(id: number): Observable<Address> {
    return this.http.get<Address>(this.addressBaseUri + '/' + id);
  }

  addAddress(address: Address): Observable<Address> {
    return this.http.post<Address>(this.addressBaseUri, address);
  }

  /**
   * Searches for all addresses in the backend with pagination
   */
  searchAddresses(page: number, size: number, name: string, lineOne: string, lineTwo: string, city: string,
                  postCode: string, country: string): Observable<Address[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    if (name !== '') {
      params = params.set('name', name);
    }
    if (lineOne !== '') {
      params = params.set('lineOne', lineOne);
    }
    if (lineOne !== '') {
      params = params.set('lineTwo', lineTwo);
    }
    if (lineOne !== '') {
      params = params.set('city', city);
    }
    if (lineOne !== '') {
      params = params.set('postCode', postCode);
    }
    if (lineOne !== '') {
      params = params.set('country', country);
    }

    return this.http.get<Address[]>(this.addressBaseUri, { params });
  }
}
