import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { Subject, takeUntil } from 'rxjs';
import { TicketService } from '../../services/ticket.service';
import { TicketDto } from '../../models/ticketDto';

@Component({
  selector: 'app-punch-ticket',
  templateUrl: './punch-ticket.component.html',
  styleUrls: ['./punch-ticket.component.scss']
})
export class PunchTicketComponent implements OnInit {

  ticket: TicketDto | null;
  destroy$ = new Subject();
  isPunching: boolean = false;
  rideId: number;

  constructor(
    private ticketService: TicketService,
    private authService: AuthService,
    private router: Router
  ) { }
  
  ngOnInit(): void {
    this.ticket = this.ticketService.punchSelectedTicket;
  }
  
  ngOnDestroy(): void {
    this.ticketService.punchSelectedTicket = null;
    this.destroy$.next(true);
    this.destroy$.complete();
  }

  punch() {
    if (!this.isPunching) {
      this.isPunching = true;
      this.ticketService.punchTicket(this.ticket!.ticketNo, this.rideId)
        .pipe(takeUntil(this.destroy$))
        .subscribe(result => {
          this.router.navigate(['tickets', 'punch', 'success']);
        });
    }
  }

}
