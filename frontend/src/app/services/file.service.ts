import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {CustomFile} from '../dtos/customFile';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  private uri: string = this.globals.backendUri + '/files';

  constructor(private httpClient: HttpClient,
              private globals: Globals) { }

  // ----- CustomFile to File converter ----
  public static asFile(customFile: CustomFile, type: string): File {
    return this.blobToFile(this.bytesToBlob(this.base64ToArrayBuffer(customFile), type));
  }
  private static base64ToArrayBuffer(base64: any): Uint8Array {
    const binaryString = window.atob(base64);
    const binaryLen = binaryString.length;
    const bytes = new Uint8Array(binaryLen);
    for (let i = 0; i < binaryLen; i++) {
      bytes[i] = binaryString.charCodeAt(i);
    }
    return bytes;
  }
  private static bytesToBlob(bytes: Uint8Array, type: string): Blob {
    return new Blob([bytes], {type});
  };
  private static blobToFile(blob: Blob): File {
    const b: any = blob;
    b.lastModifiedDate = new Date();
    b.name = 'File';
    return b;
  }
  // ----- Converter above -----

  upload(file: File): Observable<CustomFile> {
    const formData = new FormData();
    formData.append('file', file);
    return this.httpClient.post<CustomFile>(this.uri, formData);
  }

}
