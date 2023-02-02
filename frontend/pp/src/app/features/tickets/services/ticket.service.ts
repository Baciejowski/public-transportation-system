import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OfferedTicketDto } from '../models/offeredTicketDto';
import { TicketOfferDto } from '../models/ticketOfferDto';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  public selectedTicket: OfferedTicketDto | null = null;

  constructor(private http: HttpClient) { }

  getTicketOffers(): Observable<TicketOfferDto[]> {
    return this.http.get<TicketOfferDto[]>("/api/ticket/tickets/offer");
  }

  buyTickets(ticketId: number, amount: number) {
    return this.http.post(`/api/ticket/tickets/offer/buy?offeredTicketId=${ticketId}&quantity=${amount}`, {});
  }
}
