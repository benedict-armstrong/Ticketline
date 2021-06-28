import {Component, Input, OnInit} from '@angular/core';
import {LayoutUnitSelect} from '../models/layoutUnitSelect';
import {Ticket} from '../../../dtos/ticket';
import { TicketService } from 'src/app/services/ticket.service';
import {Performance} from '../../../dtos/performance';
import {NewTicket} from '../../../dtos/newTicket';
import {Venue} from '../../../dtos/venue';

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
  error = false;
  errorMessage: string;

  constructor(private ticketService: TicketService) {
  }

  ngOnInit(): void {
    this.ticketService.updateShoppingCart().subscribe(() => {
        const temp = this.performance.venue as Venue;
        for (const row of temp.layout) {
          for (const unit of row) {
            if (unit) {
              unit.sector = temp.sectors.find(s => s.id === unit.sector);
            }
          }
        }
        this.layout = temp.layout;
      },
    (error) => {
      console.error(error);
      this.defaultServiceErrorHandling(error);
    });

    this.ticketService.cartState$.subscribe(
      (data) => {
        this.selectedTickets = [].concat(...data).filter((t: Ticket) => t.performance.id === this.performance.id);
      }
    );
  }

  addSeat(seat: LayoutUnitSelect): void {
    this.vanishAlert();
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
    console.log(ticket);
    this.updatingCart = true;
    this.ticketService.addTicket(ticket).subscribe(
      () => {
        this.updatingCart = false;
      },
      (error) => {
        console.error(error);
        this.defaultServiceErrorHandling(error);
        this.updatingCart = false;
      }
    );
  }

  removeSeat(seat: LayoutUnitSelect): void {
    this.vanishAlert();
    this.updatingCart = true;
     this.ticketService.removeTicketBySeatAndPerformance(seat, this.performance).subscribe(
      () => {
        this.updatingCart = false;
      },
      (error) => {
        console.error(error);
        this.defaultServiceErrorHandling(error);
        this.updatingCart = false;
      }
    );
  }

  updateTicketType(ticket: Ticket) {
    this.vanishAlert();
    this.updatingCart = true;
    this.ticketService.updateTicketType(ticket).subscribe(
      () => {
        this.updatingCart = false;
      },
      (error) => {
        console.error(error);
        this.defaultServiceErrorHandling(error);
        this.updatingCart = false;
      }
    );
  }

  vanishAlert() {
    this.error = false;
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }
}
