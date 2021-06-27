import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
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
   * @return user with this id
   */
  getUserById(id: number): Observable<User> {
    // console.log('Load user details for ' + id);
    return this.httpClient.get<User>(this.userBaseUri + '/id/' + id);
  }

  /**
   * Loads name of user user from the backend
   *
   * @param id of user to load
   * @return user with this id
   */
  getUserByIdForConfirmation(id: number): Observable<User> {
    // console.log('Load user details for ' + id);
    return this.httpClient.get<User>(this.userBaseUri + '/confirmation/' + id);
  }

  /**
   * Loads specific user from the backend
   *
   * @param email of user to load
   */
   getUserByEmail(email: string): Observable<User> {
     // console.log('Load user details for ' + email);
     return this.httpClient.get<User>(this.userBaseUri + '/' + email);
   }

  /**
   * Persists user to the backend
   *
   * @param user to persist
   */
  createUser(user: User): Observable<User> {
    // console.log('Create user with email ' + user.email);
    return this.httpClient.post<User>(this.userBaseUri, user);
  }

  /**
   * Update user in the backend
   *
   * @param user to update
   */
  updateUser(user: User): Observable<User> {
    // console.log('Update user with email ' + user.email);
    return this.httpClient.put<User>(this.userBaseUri, user);
  }

  updateLastRead(user: User, newsId: number): Observable<User> {
    // console.log('Update user\'s last read');
    return this.httpClient.put<User>(this.userBaseUri + '/' + user.id, newsId);
  }

  /**
   * Send reset password link to email in the backend
   *
   * @param email to update
   */
  sendResetLink(email: string): Observable<void> {
    return this.httpClient.post<void>(this.userBaseUri + '/reset', email);
  }

  /**
   * change password of user identified by token
   *
   * @param password the new password
   * @param token used to identify user
   */
  changePassword(password: string, token: string): Observable<void> {
    return this.httpClient.put<void>(this.userBaseUri + '/reset', {
      password,
      token
    });
  }

  /**
   * Loads all users on a specific page.
   *
   * @param page the page number (offset).
   * @param size amount of users to load (limit).
   */
  getAll(page: number, size: number): Observable<User[]> {
    let params = new HttpParams();
    params = params.set('page', String(page));
    params = params.set('size', String(size));
    return this.httpClient.get<User[]>(this.userBaseUri, {params});
  }

  /**
   * Deletes a user from the application.
   *
   * @param id the id of the user to be removed.
   */
  delete(id: number): Observable<any> {
    return this.httpClient.delete(this.userBaseUri + '/' + id);
  }

}
