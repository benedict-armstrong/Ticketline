import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {UserService} from '../services/user.service';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StatusGuard implements CanActivate {

  constructor(private authService: AuthService,
              private userService: UserService,
              private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    if (this.authService.isLoggedIn()) {
      this.userService.getUserByEmail(this.authService.getUserEmail()).subscribe(
        response => {
          if (response.status === 'BANNED') {
            //console.log('No access due to ban');
            this.router.navigate(['/banned']);
            this.authService.logoutUser();
            return false;
          } else {
            return true;
          }
        }, _ => false
      );
    } else {
      return of(true);
    }
  }

}
