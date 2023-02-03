import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OfferedTicketDto } from '../models/offeredTicketDto';
import { TicketDto } from '../models/ticketDto';
import { TicketOfferDto } from '../models/ticketOfferDto';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  public buySelectedTicket: OfferedTicketDto | null = null;
  public punchSelectedTicket: TicketDto | null = null;

  constructor(private http: HttpClient) { }

  getTicketOffers(): Observable<TicketOfferDto[]> {
    return this.http.get<TicketOfferDto[]>("/api/ticket/tickets/offer");
  }

  buyTickets(ticketId: number, amount: number) {
    return this.http.post(`/api/ticket/tickets/offer/buy?offeredTicketId=${ticketId}&quantity=${amount}`, {});
  }

  getTickets(): Observable<TicketDto[]> {
    return this.http.get<TicketDto[]>("/api/ticket/tickets");
  }

  punchTicket(ticketId: number, rideId: number) {
    return this.http.patch(`/api/ticket/tickets/punch?ticketId=${ticketId}&rideId=${rideId}`, {});
  }
}
