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
  altTickets: Ticket[] = [];
  performance: Performance;
  eventType = 'Cinema';

  old = false;
  tooLong = false;

  @Input() set ticketItem(item: TicketGroup) {
    this.item = item;
    this.tickets = item.tickets.sort((x, y) => parseInt(x.seat.customLabel, 10) - parseInt(y.seat.customLabel, 10));
    this.performance = item.tickets[0].performance;
    this.old = item.old;

    if (this.tickets.length > 5) {
      this.tooLong = true;
      this.tickets.forEach(ticket => this.createAltTickets(ticket));
    }
    console.log(this.altTickets);

    // Formatting Eventtype
    // this.eventType = item.event.eventType.charAt(0) + item.event.eventType.slice(1).toLowerCase();
  }

  constructor() { }

  ngOnInit(): void {
  }

  createAltTickets(ticket) {
    const ticketGroup = this.altTickets.find(i => i.ticketType.id === ticket.ticketType.id);
    if (ticketGroup == null) {
      const newGroup = new Ticket(1, ticket.ticketType, this.performance, ticket.seat);
      this.altTickets.push(newGroup);
    } else {
      ticketGroup.id += 1;
    }
  }
}
