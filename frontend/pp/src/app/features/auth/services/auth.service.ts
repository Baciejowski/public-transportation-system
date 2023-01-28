import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { LoginDto } from '../models/loginDto';
import { RegisterDto } from '../models/registerDto';
import { HttpClient } from '@angular/common/http';
import * as moment from "moment";
import { catchError, map, shareReplay, tap } from 'rxjs/operators';
import { AuthResultDto } from '../models/authResultDto';
import { UserInfoDto } from '../models/userInfoDto';
import jwtDecode from 'jwt-decode';
import { JwtDto } from '../models/jwtDto';

interface SessionKeys {
  refreshToken: string,
  sessionExpirationDate: string
}

class Session {
  static instance() {
    return new Session({
      refreshToken: "23fd",
      sessionExpirationDate: "d3nt"
    });
  }

  private keys: SessionKeys

  private constructor(keys: SessionKeys) {
    this.keys = keys
  }

  exists(): boolean {
    return this.getSessionExpirationDate() != null;
  }

  isExpired(): boolean {
    return this.exists()
      ? moment().isAfter(this.getSessionExpirationDate())
      : true;
  }

  set(refreshToken: string, sessionExpiresAt: string): void {
    localStorage.setItem(this.keys.refreshToken, refreshToken);
    localStorage.setItem(this.keys.sessionExpirationDate, sessionExpiresAt);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.keys.refreshToken);
  }

  getSessionExpirationDate(): string | null {
    return localStorage.getItem(this.keys.sessionExpirationDate);
  }

  close(): void {
    localStorage.removeItem(this.keys.refreshToken);
    localStorage.removeItem(this.keys.sessionExpirationDate);
  }
}


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  public session = Session.instance();

  constructor(private http: HttpClient) {}

  private setSession(authResult: AuthResultDto) {
    this.session.set(authResult.refreshToken, authResult.accessTokenExpirationDate);
  }

  isLoggedIn(): boolean {
    return !this.session.isExpired()
  }

  getUserInfo(): Observable<UserInfoDto | null> {
    const token = this.session.getRefreshToken();
    if (token) {
      return this.http.get<UserInfoDto>("/api/account/account");
    } else {
      return of(null);
    }
  }

  refresh(): Observable<boolean> {
    if (!this.session.exists())
      return of(false); // cannot refresh not existing session

    const headers = { headers: { "Authorization": `bearer ${this.session.getRefreshToken()}` } }
    return this.http.post<AuthResultDto>("/api/account/auth/refresh", "", headers).pipe(
      map(authResult => {this.setSession(authResult); return true;}),
      catchError((e, _) => {this.logout(); return of(false);})
    );
  }

  login(loginDto: LoginDto): Observable<AuthResultDto> {
    return this.http.post<AuthResultDto>("/api/account/auth/login", loginDto)
      .pipe(
        tap(res => this.setSession(res)),
        shareReplay()
      );
  }

  register(registerDto: RegisterDto): Observable<AuthResultDto> {
    return this.http.post<AuthResultDto>("/api/account/auth/register", registerDto)
      .pipe(
        tap(res => this.setSession(res)),
        shareReplay()
      )
  }

  test(): Observable<string> {
    return this.http.get<string>("/api/account/test");
  }

  logout(): void {
    this.session.close();
  }
}