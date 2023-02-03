import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { catchError, mergeMap, of, Subject, takeUntil, tap } from 'rxjs';
import { LoginDto } from '../../models/loginDto';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  constructor(private authService: AuthService, private router: Router, private _snackBar: MatSnackBar, private translateService: TranslateService) { }
  
  ngOnInit(): void {
    this.authService.error$.subscribe(error => {
      const errorMessage = error as string;
      const text = errorMessage === "Cannot access" ? this.translateService.instant("AUTH.ACCESS") : errorMessage;
      this._snackBar.open(text, "OK", {duration: 5000})
    });
  }

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
        this.router.navigateByUrl('/validation');
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }
}
