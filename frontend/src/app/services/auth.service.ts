import { Injectable } from '@angular/core';
import { AuthRequest } from '../dtos/auth-request';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import jwt_decode from 'jwt-decode';
import { Globals } from '../global/globals';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private authBaseUri: string = this.globals.backendUri + '/authentication';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient
      .post(this.authBaseUri, authRequest, { responseType: 'text' })
      .pipe(tap((authResponse: string) => this.setToken(authResponse)));
  }

  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn(): boolean {
    return (
      !!this.getToken() &&
      this.getTokenExpirationDate(this.getToken()).valueOf() >
        new Date().valueOf()
    );
  }

  logoutUser(): void {
    console.log('Logout');
    localStorage.removeItem('authToken');
  }

  getToken(): string {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_ORGANIZER')) {
        return 'ORGANIZER';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  /**
   * Returns the email of the user based on the current token
   */
  getUserEmail() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      return decoded.sub;
    }
    return null;
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private getTokenExpirationDate(token: string): Date {
    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }
}
