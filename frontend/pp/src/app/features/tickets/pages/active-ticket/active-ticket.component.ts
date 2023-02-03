import { Component, OnInit } from '@angular/core';
import { TicketDto } from '../../models/ticketDto';
import { TicketService } from '../../services/ticket.service';

@Component({
  selector: 'app-active-ticket',
  templateUrl: './active-ticket.component.html',
  styleUrls: ['./active-ticket.component.scss']
})
export class ActiveTicketComponent implements OnInit {

  ticket: TicketDto | null;
  isPunching: boolean = false;
  rideId: number;

  constructor(private ticketService: TicketService) { }
  
  ngOnInit(): void {
    this.ticket = this.ticketService.punchSelectedTicket;
  }
  
  ngOnDestroy(): void {
    this.ticketService.punchSelectedTicket = null;
  }
}
