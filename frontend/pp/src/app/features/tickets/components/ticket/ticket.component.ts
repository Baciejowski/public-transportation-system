import { Component, OnInit, Input } from '@angular/core';
import * as moment from 'moment';
import { OfferedTicketDto } from '../../models/offeredTicketDto';
import { TicketDto } from '../../models/ticketDto';

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.scss']
})
export class TicketComponent implements OnInit {
  type: string = '';
  time: string = '';
  subtitleThin: string = '';
  subtitleBold: string = '';
  rideId: number;
  @Input() ticket: OfferedTicketDto | TicketDto;

  constructor() { }

  ngOnInit(): void {

    this.type = this.ticket.isReduced ? "TICKET.REDUCED" : "TICKET.REGULAR";
    this.time = this.ticket.duration ? this.calculateTime(this.ticket.duration) : "TICKET.ONEWAY";
    const punchedTicket = this.ticket as TicketDto;
    if (punchedTicket.punched && punchedTicket.rideId) {
      this.rideId = punchedTicket.rideId;
      this.subtitleThin = "TICKET.RIDE";
    } else {
      const price = (this.ticket as OfferedTicketDto).price ?
                    (this.ticket as OfferedTicketDto).price :
                    (this.ticket as TicketDto).pricePaid;
      this.subtitleBold = `${price.toFixed(2)} zł`;
    }
  }

  calculateTime(time: string): string {
    const duration = moment.duration(time);
    let result = '';
    if (duration.hours() || duration.days()) {
      result += `${duration.hours() + duration.days() * 24}h`;
    }
    if (duration.minutes()) {
      result += ` ${duration.minutes()} min`
    }
    return result;
  }
}
