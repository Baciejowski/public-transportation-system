import { Component, OnDestroy, OnInit } from '@angular/core';
import { map, Observable, Subject } from 'rxjs';
import { TicketOffer } from '../../models/ticketOfferDto';
import { TicketService } from '../../services/ticket.service';

@Component({
  selector: 'app-ticket-offer',
  templateUrl: './ticket-offer.component.html',
  styleUrls: ['./ticket-offer.component.scss']
})
export class TicketOfferComponent implements OnInit, OnDestroy {
  ticketOffer$: Observable<TicketOffer>;
  destroy$ = new Subject();

  constructor(private ticketService: TicketService) { }

  ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }

  ngOnInit(): void {
    this.ticketOffer$ = this.ticketService.getTicketOffers().pipe(
      map(ticketOffers => ticketOffers.find(offer => offer.currentlyValid)!)
    )
  }



}
