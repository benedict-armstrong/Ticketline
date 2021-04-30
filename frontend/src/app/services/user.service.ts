import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../dtos/user';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class ApplicationUserService {

  private userBaseUri: string = this.globals.backendUri + '/users';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all messages from the backend
   */
  getUser(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.userBaseUri);
  }

  /**
   * Loads specific message from the backend
   *
   * @param id of message to load
   */
  getUserById(id: number): Observable<User> {
    console.log('Load user details for ' + id);
    return this.httpClient.get<User>(this.userBaseUri + '/' + id);
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
