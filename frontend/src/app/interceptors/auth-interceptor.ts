import { Injectable } from "@angular/core";
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from "@angular/common/http";
import { AuthService } from "../services/auth.service";
import { Observable } from "rxjs";
import { Globals } from "../global/globals";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private globals: Globals) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const authUri = this.globals.backendUri + "/authentication";

    // Do not intercept authentication requests or if no user is logged in
    if (req.url === authUri || !this.authService.isLoggedIn()) {
      console.log("Unauthenticated Request");
      return next.handle(req);
    }

    const authReq = req.clone({
      headers: req.headers.set(
        "Authorization",
        "Bearer " + this.authService.getToken()
      ),
    });

    return next.handle(authReq);
  }
}
