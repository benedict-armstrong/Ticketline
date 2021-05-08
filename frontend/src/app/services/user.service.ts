import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../dtos/user';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userBaseUri: string = this.globals.backendUri + '/users';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Persists user to the backend
   *
   * @param user to persist
   */
  createUser(user: User): Observable<User> {
    console.log('Create user with email ' + user.email);
    return this.httpClient.post<User>(this.userBaseUri, user);
  }
}
