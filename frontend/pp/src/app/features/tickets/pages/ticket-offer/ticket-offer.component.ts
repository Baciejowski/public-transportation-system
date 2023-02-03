import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { map, Observable, Subject, takeUntil } from 'rxjs';
import { OfferedTicketDto } from '../../models/offeredTicketDto';
import { TicketOfferDto } from '../../models/ticketOfferDto';
import { TicketService } from '../../services/ticket.service';

@Component({
  selector: 'app-ticket-offer',
  templateUrl: './ticket-offer.component.html',
  styleUrls: ['./ticket-offer.component.scss']
})
export class TicketOfferComponent implements OnInit, OnDestroy {
  discounted: boolean = false;
  destroy$ = new Subject();
  oneWayTickets: OfferedTicketDto[] = [];
  timeLimitedTickets: OfferedTicketDto[] = [];

  constructor(private ticketService: TicketService, private router: Router) { }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }

  ngOnInit(): void {
    this.ticketService.getTicketOffers()
      .pipe(
        map(ticketOffers => ticketOffers.find(offer => offer.currentlyValid)!),
        takeUntil(this.destroy$)
      )
      .subscribe(ticketOffer => {
        ticketOffer.tickets.forEach(ticket => {
          if (ticket.duration) {
            this.timeLimitedTickets.push(ticket);
          } else {
            this.oneWayTickets.push(ticket);
          }
        });
        this.oneWayTickets.sort((a, b) => a.price - b.price);
        this.timeLimitedTickets.sort((a, b) => a.price - b.price);
      });
    
  }

  onToggleChange(value: string) {
    if (value === 'discounted') {
      this.discounted = true;
    } else {
      this.discounted = false;
    }
  }

  onTicketSelect(ticket: OfferedTicketDto) {
    this.ticketService.buySelectedTicket = ticket;
    this.router.navigate(['tickets', 'buy']);
  }

}
