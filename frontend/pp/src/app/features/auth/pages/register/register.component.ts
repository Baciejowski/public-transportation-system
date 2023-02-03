import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { Router } from '@angular/router';
import { catchError, of, Subject, takeUntil } from 'rxjs';
import { RegisterDto } from '../../models/registerDto';
import { AuthService } from '../../services/auth.service';


export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null): boolean {
    const invalidCtrl = !!(control && control.invalid && control.parent?.dirty);
    const invalidParent = !!(control && control.parent && control.parent.invalid && control.parent.dirty);

    return (invalidCtrl || invalidParent);
  }
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnDestroy {

  constructor(private authService: AuthService, private router: Router) { }

  matcher = new MyErrorStateMatcher();
  destroy$ = new Subject();

  checkPasswords: ValidatorFn = (group: AbstractControl):  ValidationErrors | null => { 
    let pass = group.get('password')?.value;
    let confirmPass = group.get('confirmPassword')?.value
    return pass === confirmPass ? null : { notSame: true }
  }

  registerForm = new FormGroup({
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    fullPassword: new FormGroup({
      password: new FormControl('', [Validators.required]),
      confirmPassword: new FormControl('', [Validators.required])
    }, {
      validators: this.checkPasswords
    })
  });

  get firstName() { return this.registerForm.get("firstName") }
  get lastName() { return this.registerForm.get("lastName") }
  get email() { return this.registerForm.get("email") }
  get fullPassword() { return this.registerForm.get("fullPassword") }
  get password() { return this.registerForm.get("fullPassword.password") }
  get confirmPassword() { return this.registerForm.get("fullPassword.confirmPassword") }

  onSubmit() {
    const registerDto = {
      email: this.registerForm.get("email")!.value,
      firstName: this.registerForm.get("firstName")!.value,
      lastName: this.registerForm.get("lastName")!.value,
      password: this.registerForm.get("fullPassword.password")!.value
    } as RegisterDto;
    this.authService.register(registerDto)
      .pipe(
        takeUntil(this.destroy$),
      )
      .subscribe(res => {
        this.authService.initUserInfo();
        this.router.navigateByUrl('/tickets/offer')
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }
}
