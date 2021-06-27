import {Component, Input, OnInit} from '@angular/core';
import {LayoutUnitSelect} from '../models/layoutUnitSelect';
import {Ticket} from '../../../dtos/ticket';
import { TicketService } from 'src/app/services/ticket.service';
import {Performance} from '../../../dtos/performance';
import {NewTicket} from '../../../dtos/newTicket';

@Component({
  selector: 'app-select-seat',
  templateUrl: './select-seat.component.html',
  styleUrls: ['./select-seat.component.scss']
})
export class SelectSeatComponent implements OnInit {

  @Input() performance: Performance;

  layout: LayoutUnitSelect[][];

  selectedTickets: Ticket[] = [];

  updatingCart = false;

  constructor(private ticketService: TicketService) {
  }

  ngOnInit(): void {
    this.ticketService.updateShoppingCart().subscribe(() => {
        for (const row of this.performance.venue.layout) {
          for (const unit of row) {
            if (unit) {
              unit.sector = this.performance.venue.sectors.find(s => s.id === unit.sector);
            }
          }
        }
        this.layout = this.performance.venue.layout;
      },
    (error) => {
      console.error(error);
    });
  }

  addSeat(seat: LayoutUnitSelect): void {
    const ticketType = this.performance.ticketTypes.find(
      (tt) =>
        // @ts-ignore
        seat.sector.hasOwnProperty('id') ? tt.sector.id === seat.sector.id : tt.sector.id === seat.sector
    );
    const ticket = new NewTicket(
      this.performance.id,
      ticketType,
      1,
      seat.id
    );
    this.updatingCart = true;
    this.ticketService.addTicket(ticket).subscribe(
      (data) => {
        this.selectedTickets = [].concat(...data).filter((t: Ticket) => t.performance.id === this.performance.id);
        this.updatingCart = false;
      },
      (error) => {
        console.error(error);
        this.updatingCart = false;
      }
    );
  }

  removeSeat(seat: LayoutUnitSelect): void {
    this.updatingCart = true;
    this.ticketService.removeTicketBySeat(seat).subscribe(
      (data) => {
        this.selectedTickets = [].concat(...data).filter((t: Ticket) => t.performance.id === this.performance.id);
        this.updatingCart = false;
      },
      (error) => {
        console.error(error);
        this.updatingCart = false;
      }
    );
    // const index = this.selectedTickets.findIndex(
    //   (ticket) =>  ticket.seat === seat
    // );
    // if (index > -1) {
    //   this.selectedTickets.splice(index, 1);
    // }
  }
}
