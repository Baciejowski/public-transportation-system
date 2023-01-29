import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { mergeMap, Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class SessionInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let token = this.authService.session.getRefreshToken();
    if (token) {
      req = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
    }

    console.log(req.url, req.headers);

    if (this.authService.isLoggedIn() || req.url.includes("refresh"))
      return next.handle(req);

    return this.authService.refresh().pipe(mergeMap(_ => {
      token = this.authService.session.getRefreshToken();
      if (token) {
        req = req.clone({
          setHeaders: { Authorization: `Bearer ${token}` }
        });
      }
      return next.handle(req)
    }));
  }
}
