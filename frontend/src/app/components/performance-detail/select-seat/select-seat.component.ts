import {Component, Input, OnInit} from '@angular/core';
import {LayoutUnitSelect} from '../models/layoutUnitSelect';
import {Ticket} from '../../../dtos/ticket';
import { TicketService } from 'src/app/services/ticket.service';
import {Performance} from '../../../dtos/performance';

@Component({
  selector: 'app-select-seat',
  templateUrl: './select-seat.component.html',
  styleUrls: ['./select-seat.component.scss']
})
export class SelectSeatComponent implements OnInit {

  @Input() performance: Performance;

  layout: LayoutUnitSelect[][];

  selectedTickets: Ticket[] = [];

  constructor(private ticketService: TicketService) {
  }

  ngOnInit(): void {
    this.ticketService.updateShoppingCart().subscribe(() => {
      for (const row of this.venue.layout) {
        for (const unit of row) {
          if (unit) {
            unit.sector = this.venue.sectors.find(s => s.id === unit.sector);
          }
        }
      }
      this.layout = this.venue.layout;
    },
    (error) => {
    });
    for (const row of this.performance.venue.layout) {
      for (const unit of row) {
        if (unit) {
          unit.sector = this.performance.venue.sectors.find(s => s.id === unit.sector);
        }
      }
    }

    this.layout = this.performance.venue.layout;
  }

  addSeat(seat: LayoutUnitSelect): void {
    this.selectedTickets.push(
      new Ticket(
        null,
        null,
        this.performance,
        seat
      )
    );
  }

  removeSeat(seat: LayoutUnitSelect): void {
    const index = this.selectedTickets.findIndex(
      (ticket) =>  ticket.seat === seat
    );
    if (index > -1) {
      this.selectedTickets.splice(index, 1);
    }
  }

  // onClick(): void {
  //   if (this.layoutUnit === null) {
  //     return;
  //   }
  //   const sector: Sector = this.layoutUnit.sector as Sector;
  //   if (sector.type === 'STAGE') {
  //     return;
  //   }
  //   if (!this.layoutUnit.free) {
  //     return;
  //   }
  //
  //   const addTicket: NewTicket = {
  //     performanceId: this.performanceId,
  //     ticketType: null,
  //     amount: null,
  //     seatId: this.layoutUnit.id
  //   };
  //   this.ticketService.addTicket(addTicket).subscribe(
  //     (responseTickets: Ticket[]) => {
  //
  //       let done = false;
  //       for (let i = 0; i < this.ticketService.cart.length; i++) {
  //         if (this.ticketService.cart[i].length === 0) {
  //           this.ticketService.cart[i] = responseTickets;
  //           done = true;
  //           break;
  //         } else {
  //           if (this.ticketService.cart[i][0].performance.id === responseTickets[0].performance.id) {
  //             responseTickets.forEach(ticket => {
  //               this.ticketService.cart[i].push(ticket);
  //             });
  //             done = true;
  //             break;
  //           }
  //         }
  //       }
  //
  //       if (!done) {
  //         this.ticketService.cart.push(responseTickets);
  //       }
  //       this.ticketService.updatePrice();
  //     },
  //     (error) => {
  //     }
  //   );
  // }
}
