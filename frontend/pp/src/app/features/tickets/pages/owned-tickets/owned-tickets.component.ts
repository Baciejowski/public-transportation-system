import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { TicketDto } from '../../models/ticketDto';
import { TicketService } from '../../services/ticket.service';

@Component({
  selector: 'app-owned-tickets',
  templateUrl: './owned-tickets.component.html',
  styleUrls: ['./owned-tickets.component.scss']
})
export class OwnedTicketsComponent implements OnInit {

  destroy$ = new Subject();
  oneWayTickets: TicketDto[] = [];
  timeLimitedTickets: TicketDto[] = [];
  loading: boolean = false;

  constructor(private ticketService: TicketService, private router: Router) { }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }

  ngOnInit(): void {
    this.loading = true;
    this.ticketService.getTickets()
      .pipe(takeUntil(this.destroy$))
      .subscribe(tickets => {
        tickets.forEach(ticket => {
          if (ticket.punched) return;
          if (ticket.duration) {
            this.timeLimitedTickets.push(ticket);
          } else {
            this.oneWayTickets.push(ticket);
          }
        });
        this.loading = false;
      });
    
  }

  onTicketSelect(ticket: TicketDto) {
    this.ticketService.punchSelectedTicket = ticket;
    this.router.navigate(['tickets', 'punch']);
  }

}
