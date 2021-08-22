import {Injectable, Injector} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {from, Observable, of} from 'rxjs';
import {concatMap, first} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class MasterGuard implements CanActivate {

  constructor(private injector: Injector) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return from(route.data.guards).pipe(concatMap((value) => {
      const guard = this.injector.get(value); // init guard
      const result = guard.canActivate(route, state); // call canActivate
      if (result instanceof Observable) {
        return result;
      } else if (result instanceof Promise) {
        return from(result); // convert Promise to Observable
      } else {
        return of(result); // create Observable
      }
    }), first((x) => x === false, true));
  }

}
