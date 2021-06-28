import { Component, OnInit } from '@angular/core';
import { TopEvent } from 'src/app/dtos/topEvent';
import {ApplicationEventService} from '../../services/event.service';
import {TicketService} from '../../services/ticket.service';
import {GraphData} from '../graphs/graphData';

@Component({
  selector: 'app-explore',
  templateUrl: './explore.component.html',
  styleUrls: ['./explore.component.scss']
})
export class ExploreComponent implements OnInit {

  topEvents: TopEvent[];
  sales: number[];
  data: GraphData;

  constructor(private eventService: ApplicationEventService, private ticketService: TicketService) { }

  ngOnInit(): void {
    this.getEvents();
    this.getSales();
  }

  getEvents() {
    this.eventService.getTopEvents().subscribe(
      (data) => {
        this.topEvents = data;
        this.topEvents.sort((a, b) => b.soldTickets/b.totalTickets - a.soldTickets/a.totalTickets);
        this.topEvents.splice(5, this.topEvents.length - 5);
      }
    );
  }

  getSales() {
    this.ticketService.getSales().subscribe(
      (data) => this.data = {
        name: 'Ticket sales past 7 days',
        values: data,
        xLabels: this.getDays()
      }
    );
  }

  getDays(): string[] {
    const days = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
    const today = new Date();
    const out = [];
    for (let i = 6; i >= 0; i--) {
      const day = new Date();
      day.setDate(today. getDate() - i);
      out.push(days[day.getDay()]);
    }
    return out;
  }

}
