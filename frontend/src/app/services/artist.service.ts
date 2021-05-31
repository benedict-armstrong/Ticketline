import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Artist} from '../dtos/artist';

@Injectable({
  providedIn: 'root'
})
export class ArtistService {
  private artistBaseUri: string = this.globals.backendUri + '/artists';

  constructor(private http: HttpClient, private globals: Globals) { }

  getArtists(page: number, size: number): Observable<Artist[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    return this.http.get<Artist[]>(this.artistBaseUri, {params});
  }

  getArtistById(id: number): Observable<Artist> {
    return this.http.get<Artist>(this.artistBaseUri + '/' + id);
  }

  addArtist(artist: Artist): Observable<Artist> {
    return this.http.post<Artist>(this.artistBaseUri, artist);
  }

  /**
   * Searches for all artists in the backend with pagination
   */
  searchArtists(page: number, size: number, firstName: string, lastName: string): Observable<Artist[]> {
    let params = new HttpParams();
    console.log(page);
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    if (firstName !== '') {
      params = params.set('firstName', firstName);
    }
    if (lastName !== '') {
      params = params.set('lastName', lastName);
    }

    return this.http.get<Artist[]>(this.artistBaseUri, { params });
  }
}
