import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TicketDto } from '../../models/ticketDto';
import { Subject, takeUntil } from 'rxjs';
import { TicketService } from '../../services/ticket.service';

@Component({
  selector: 'app-active-tickets',
  templateUrl: './active-tickets.component.html',
  styleUrls: ['./active-tickets.component.scss']
})
export class ActiveTicketsComponent implements OnInit {

  destroy$ = new Subject();
  oneWayTickets: TicketDto[] = [];
  timeLimitedTickets: TicketDto[] = [];

  constructor(private ticketService: TicketService, private router: Router) { }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }

  ngOnInit(): void {
    this.ticketService.getTickets()
      .pipe(takeUntil(this.destroy$))
      .subscribe(tickets => {
        tickets.forEach(ticket => {
          if (!ticket.punched) return;
          if (ticket.duration) {
            this.timeLimitedTickets.push(ticket);
          } else {
            this.oneWayTickets.push(ticket);
          }
        });
      });
    
  }

  onTicketSelect(ticket: TicketDto) {
    this.ticketService.punchSelectedTicket = ticket;
    this.router.navigate(['tickets', 'active', 'info']);
  }

}
