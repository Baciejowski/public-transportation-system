import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.scss']
})
export class TicketComponent implements OnInit {
  @Input() type: string = '';
  @Input() time: string = '';
  @Input() subtitleThin: string = '';
  @Input() subtitleBold: string = '';

  constructor() { }

  ngOnInit(): void {
  }

}
