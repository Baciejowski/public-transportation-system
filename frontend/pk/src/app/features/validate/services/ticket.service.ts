import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  constructor(private http: HttpClient) { }

  validateTicket(ticketId: number, rideId: number): Observable<boolean> {
    return this.http.get<boolean>(`/api/ticket/tickets/validate?ticketId=${ticketId}&rideId=${rideId}`);
  }
}
