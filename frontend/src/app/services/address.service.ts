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

  findAllByCriteria(page: number, size: number): Observable<Address[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    return this.http.get<Address[]>(this.addressBaseUri, {params});
  }

  addAddress(address: Address): Observable<Address> {
    return this.http.post<Address>(this.addressBaseUri, address);
  }
}
