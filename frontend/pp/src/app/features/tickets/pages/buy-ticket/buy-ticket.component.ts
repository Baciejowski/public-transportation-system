import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subject, takeUntil } from 'rxjs';
import { UserInfoDto } from 'src/app/features/auth/models/userInfoDto';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { OfferedTicketDto } from '../../models/offeredTicketDto';
import { TicketService } from '../../services/ticket.service';

@Component({
  selector: 'app-buy-ticket',
  templateUrl: './buy-ticket.component.html',
  styleUrls: ['./buy-ticket.component.scss']
})
export class BuyTicketComponent implements OnInit, OnDestroy {

  ticket: OfferedTicketDto | null;
  count: number = 1;
  cost: number;
  destroy$ = new Subject();
  accountBalance: number = 0;
  isBuying: boolean = false;

  constructor(
    private ticketService: TicketService,
    private authService: AuthService,
    private router: Router
  ) { }
  
  ngOnInit(): void {
    this.ticket = this.ticketService.buySelectedTicket;
    this.cost = this.count * (this.ticket?.price || 0); 
    this.authService.getUserInfo()
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => this.accountBalance = user?.accountBalance || 0);
  }
  
  ngOnDestroy(): void {
    this.ticketService.buySelectedTicket = null;
    this.destroy$.next(true);
    this.destroy$.complete();
  }

  changeCount(amount: number) {
    if (amount < 0 && this.count === 1) return;
    this.count += amount;
    this.cost = this.ticket!.price * this.count;
  }

  buy() {
    if (!this.isBuying) {
      this.isBuying = true;
      this.ticketService.buyTickets(this.ticket!.id, this.count)
        .pipe(takeUntil(this.destroy$))
        .subscribe(result => {
          this.router.navigate(['tickets', 'buy', 'success']);
        });
    }
  }
}
