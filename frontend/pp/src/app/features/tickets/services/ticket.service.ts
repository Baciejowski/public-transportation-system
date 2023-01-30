import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TicketOffer } from '../models/ticketOfferDto';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  constructor(private http: HttpClient) { }

  getTicketOffers(): Observable<TicketOffer[]> {
    return this.http.get<TicketOffer[]>("/api/ticket/tickets/offer");
  }
}
