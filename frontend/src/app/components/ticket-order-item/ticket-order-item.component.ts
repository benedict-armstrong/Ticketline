import {Component, Input, OnInit} from '@angular/core';
import {TicketGroup} from '../../dtos/ticketGroup';
import {Ticket} from '../../dtos/ticket';
import {Performance} from '../../dtos/performance';
import {TicketService} from '../../services/ticket.service';

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
  eventType = '';

  old = false;
  reserved = false;
  tooLong = false;

  @Input() set ticketItem(item: TicketGroup) {
    this.item = item;
    this.tickets = item.tickets.sort((x, y) => parseInt(x.seat.customLabel, 10) - parseInt(y.seat.customLabel, 10));
    this.performance = item.tickets[0].performance;
    this.old = item.old;
    this.reserved = item.reserved;

    if (this.tickets.length > 5) {
      this.tooLong = true;
      this.tickets.forEach(ticket => this.createAltTickets(ticket));
    }

    // Formatting Eventtype
    // this.eventType = item.event.eventType.charAt(0) + item.event.eventType.slice(1).toLowerCase();
  }

  constructor(private ticketService: TicketService) { }

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

  downloadTicket() {
    this.item.tickets.forEach(
      ticket => ticket.ticketType.sector = null
    );

    this.item.tickets.forEach(
      ticket => ticket.seat.sector = null
    );

    console.log(this.item);

    this.ticketService.getTicketPdf(this.performance.id).subscribe(
      (response) => {
        const pdf = response;
        console.log(pdf);
      }, error => {
        console.error(error);
      }
    );
  }
}
