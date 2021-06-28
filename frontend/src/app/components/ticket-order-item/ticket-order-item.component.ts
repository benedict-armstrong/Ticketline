import {Component, Input, OnInit} from '@angular/core';
import {TicketGroup} from '../../dtos/ticketGroup';
import {Ticket} from '../../dtos/ticket';
import {Performance} from '../../dtos/performance';
import {TicketService} from '../../services/ticket.service';
import {FileService} from '../../services/file.service';

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
  cancelled = false;
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
  }

  constructor(private ticketService: TicketService, private fileService: FileService) { }

  ngOnInit(): void {
  }

  createAltTickets(ticket) {
    const ticketGroup = this.altTickets.find(i => i.ticketType.id === ticket.ticketType.id);
    if (ticketGroup == null) {
      const newGroup = new Ticket(1, ticket.ticketType, this.performance, ticket.seat, ticket.status);
      this.altTickets.push(newGroup);
    } else {
      ticketGroup.id += 1;
    }
  }

  download(pdf) {
    const a = document.createElement('a');
    document.body.appendChild(a);
    const url = window.URL.createObjectURL(pdf);
    a.href = url;
    a.download = this.item.tickets[0].performance.title + '.pdf';
    a.click();
    window.URL.revokeObjectURL(url);
  }

  clickDownloadTicket() {
    this.ticketService.getTicketPdf(this.performance.id).subscribe(
      (response) => {
        const pdf = response;
        this.download(FileService.asFile(pdf.data, pdf.type));
      }, error => {
        //console.error(error);
      }
    );
  }
}
