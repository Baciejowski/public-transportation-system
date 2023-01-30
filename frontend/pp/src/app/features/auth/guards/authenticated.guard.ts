import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { map, Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticatedGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {

  }

  canActivate(): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    // return this.authService.isLoggedIn() || this.authService.refresh();
    if (this.authService.isLoggedIn()) {
      return true;
    }
    return this.authService.refresh().pipe(
      map(result => {
        if (result) {
          return result;
        } else {
          return this.router.parseUrl("/login");
        }
      })
    )
  }
  
}
