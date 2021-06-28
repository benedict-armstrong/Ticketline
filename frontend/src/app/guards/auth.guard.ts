import {Injectable} from '@angular/core';
import {CanActivate, Router, ActivatedRouteSnapshot} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {UserService} from '../services/user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService,
              private userService: UserService,
              private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (this.authService.isLoggedIn()) {
      if (this.checkPermissions(route)) {
        return true;
      } else {
        this.router.navigate(['']);
        return false;
      }
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }

  checkPermissions(route: ActivatedRouteSnapshot): boolean {
    const userRole = this.authService.getUserRole();
    if (route.data.roles.includes(userRole)) {
      return true;
    }
    //console.log('Not the necessary permissions');
    return false;
  }
}
