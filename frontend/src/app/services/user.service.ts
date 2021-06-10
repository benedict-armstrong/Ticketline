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
   * Loads all messages from the backend
   */
  getUser(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.userBaseUri);
  }

  /**
   * Loads specific user from the backend
   *
   * @param id of user to load
   */
  getUserById(id: number): Observable<User> {
    //console.log('Load user details for ' + id);
    return this.httpClient.get<User>(this.userBaseUri + '/' + id);
  }

  /**
   * Loads specific user from the backend
   *
   * @param email of user to load
   */
   getUserByEmail(email: string): Observable<User> {
     //console.log('Load user details for ' + email);
     return this.httpClient.get<User>(this.userBaseUri + '/' + email);
   }

  /**
   * Persists user to the backend
   *
   * @param user to persist
   */
  createUser(user: User): Observable<User> {
    //console.log('Create user with email ' + user.email);
    return this.httpClient.post<User>(this.userBaseUri, user);
  }


  /**
   * Update user in the backend
   *
   * @param user to update
   */
  updateUser(user: User): Observable<User> {
    //console.log('Update user with email ' + user.email);
    return this.httpClient.put<User>(this.userBaseUri, user);
  }

  updateLastRead(user: User, newsId: number): Observable<User> {
    //console.log('Update user\'s last read');
    return this.httpClient.put<User>(this.userBaseUri + '/' + user.id, newsId);
  }

  /**
   * Reset password in the backend
   *
   * @param email to update
   */
  resetPassword(email: string): Observable<User> {
    //console.log('Reset password for user with email ' + email);
    return this.httpClient.put<User>(this.userBaseUri + '/reset', email);
  }
}
