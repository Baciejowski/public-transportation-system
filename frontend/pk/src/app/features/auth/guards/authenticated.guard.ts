import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, UrlTree } from '@angular/router';
import { userInfo } from 'os';
import { map, Observable, of } from 'rxjs';
import { AuthService } from '../services/auth.service';
import jwtDecode from 'jwt-decode';
import { JwtDto } from '../models/jwtDto';

@Injectable({
  providedIn: 'root'
})
export class AuthenticatedGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {

  }

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.authService.isLoggedIn()) {
      return this.hasCorrectRole(route);
    }
    
    return this.authService.refresh().pipe(
      map(result => {
        if (result) {
          return this.hasCorrectRole(route);
        } else {
          return this.router.parseUrl("/login");
        }
      })
    )
  }

  hasCorrectRole(route: ActivatedRouteSnapshot) {
    const role = (jwtDecode(this.authService.session.getRefreshToken()!) as JwtDto).role;
    if (route.data['roles'].includes(role)) {
      return true;
    }
    this.authService.error$.next("Cannot access");
    return this.router.parseUrl("/login");
  }
}
