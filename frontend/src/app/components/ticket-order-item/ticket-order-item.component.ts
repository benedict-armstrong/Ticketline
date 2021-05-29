import {Component, Input, OnInit} from '@angular/core';
import {TicketGroup} from '../../dtos/ticketGroup';
import {Ticket} from '../../dtos/ticket';

@Component({
  selector: 'app-ticket-order-item',
  templateUrl: './ticket-order-item.component.html',
  styleUrls: ['./ticket-order-item.component.scss']
})
export class TicketOrderItemComponent implements OnInit {
  item: TicketGroup;
  tickets: Ticket[];
  eventType = 'Cinema';
  test: number[];

  location = 'Location';
  startTime = '29th of April, 20:00';
  old = false;

  @Input() set ticketItem(item: TicketGroup) {
    this.item = item;
    this.tickets = item.tickets;

    // Formatting Eventtype
    this.eventType = item.event.eventType.charAt(0) + item.event.eventType.slice(1).toLowerCase();
  }

  constructor() { }

  ngOnInit(): void {
    this.test = [];
    this.test.push(1);
    this.test.push(2);
    this.test.push(3);
    this.test.push(4);
  }
}
