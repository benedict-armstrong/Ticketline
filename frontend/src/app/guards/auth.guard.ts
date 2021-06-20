import {Injectable} from '@angular/core';
import {CanActivate, Router, ActivatedRouteSnapshot} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {UserService} from '../services/user.service';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService,
              private userService: UserService,
              private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
    if (this.authService.isLoggedIn()) {
      // Check if has permissions
      if (this.checkPermissions(route)) {
        // Check if banned
        this.userService.getUserByEmail(this.authService.getUserEmail()).subscribe(
          response => {
            if (response.status === 'BANNED') {
              console.log('No access due to ban');
              this.router.navigate(['/banned']);
              return false;
            } else {
              return true;
            }
          }, error => {
            console.error(error);
            return false;
          }
        );
        return of(true);
      } else {
        this.router.navigate(['']);
        return of(false);
      }
    } else {
      this.router.navigate(['/login']);
      return of(false);
    }
  }

  checkPermissions(route: ActivatedRouteSnapshot): boolean {
    const userRole = this.authService.getUserRole();
    if (route.data.roles.includes(userRole)) {
      return true;
    }
    console.log('Not the necessary permissions');
    return false;
  }
}
