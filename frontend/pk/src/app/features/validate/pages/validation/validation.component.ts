import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Subject, takeUntil } from 'rxjs';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { TicketService } from '../../services/ticket.service';

@Component({
  selector: 'app-validation',
  templateUrl: './validation.component.html',
  styleUrls: ['./validation.component.scss']
})
export class ValidationComponent implements OnDestroy, OnInit {

  ticketId: number;
  rideId: number;
  destroy$ = new Subject();

  constructor(private ticketService: TicketService, private authService: AuthService, private _snackBar: MatSnackBar, private translateService: TranslateService) { }
  
  ngOnInit(): void {
    this.authService.error$.subscribe(error => {
      const errorMessage = error as string;
      const text = errorMessage === "Bad Request" ? this.translateService.instant("VALIDATION.NOT_PUNCHED") : errorMessage;
      this._snackBar.open(text, "OK", {duration: 5000})
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }

  validate() {
    this.ticketService.validateTicket(this.ticketId, this.rideId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(result => {
        const text = result ? this.translateService.instant("VALIDATION.VALID") : this.translateService.instant("VALIDATION.INVALID");
        this._snackBar.open(text, this.translateService.instant("VALIDATION.CLOSE"), {duration: 5000});
      });
  }
}
