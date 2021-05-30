import {Component, Input, OnInit} from '@angular/core';
import {TicketGroup} from '../../dtos/ticketGroup';
import {Ticket} from '../../dtos/ticket';
import {Performance} from '../../dtos/performance';

@Component({
  selector: 'app-ticket-order-item',
  templateUrl: './ticket-order-item.component.html',
  styleUrls: ['./ticket-order-item.component.scss']
})
export class TicketOrderItemComponent implements OnInit {
  item: TicketGroup;
  tickets: Ticket[];
  performance: Performance;
  eventType = 'Cinema';

  location = 'Location';
  startTime = '29th of April, 20:00';
  old = false;

  @Input() set ticketItem(item: TicketGroup) {
    this.item = item;
    this.tickets = item.tickets;
    this.performance = item.performance;

    // Formatting Eventtype
    this.eventType = item.event.eventType.charAt(0) + item.event.eventType.slice(1).toLowerCase();

    // Setting old
    this.old = false;
  }

  constructor() { }

  ngOnInit(): void {
  }
}
