import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { catchError, mergeMap, Observable, throwError } from 'rxjs';
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
    
    if (this.authService.isLoggedIn() || req.url.includes("refresh") || req.url.includes("assets") || req.url.includes("account/account")) {
      return next.handle(req).pipe(
        catchError((error: HttpErrorResponse) => {
          this.authService.error$.next(error.statusText);
          return throwError(() => new Error(error.status.toString()))
        })
      );
    }

    return this.authService.refresh().pipe(mergeMap(_ => {
      token = this.authService.session.getRefreshToken();
      if (token) {
        req = req.clone({
          setHeaders: { Authorization: `Bearer ${token}` }
        });
      }
      return next.handle(req).pipe(
        catchError((error: HttpErrorResponse) => {
          this.authService.error$.next(error.statusText);
          return throwError(() => new Error(error.status.toString()))
        })
      );
    }));
  }
}
