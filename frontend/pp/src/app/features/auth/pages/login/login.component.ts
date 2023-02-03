import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, mergeMap, of, Subject, takeUntil, tap } from 'rxjs';
import { LoginDto } from '../../models/loginDto';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  constructor(private authService: AuthService, private router: Router) { }

  destroy$ = new Subject();

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });
  
  get email() { return this.loginForm.get("email") }
  get password() { return this.loginForm.get("password") }

  onSubmit() {
    const loginDto = this.loginForm.value as LoginDto;
    this.authService.login(loginDto)
      .pipe(
        takeUntil(this.destroy$)    
      ).subscribe(res => {
        this.authService.initUserInfo();
        this.router.navigateByUrl('/tickets/offer');
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }
}
