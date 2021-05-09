import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  private uri: string = this.globals.backendUri + '/files';

  constructor(private httpClient: HttpClient,
              private globals: Globals) { }

  upload(file: File): Observable<number> {
    const formData = new FormData();
    formData.append('file', file);
    return this.httpClient.post<number>(this.uri, formData);
  }

}
