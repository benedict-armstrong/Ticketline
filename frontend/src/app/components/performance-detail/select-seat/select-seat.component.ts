import {Component, Input, OnInit} from '@angular/core';
import {Venue} from '../../../dtos/venue';
import {LayoutUnitSelect} from '../models/layoutUnitSelect';
import {Sector} from '../../../dtos/sector';
import {NewTicket} from '../../../dtos/newTicket';
import {Ticket} from '../../../dtos/ticket';
import { TicketService } from 'src/app/services/ticket.service';

@Component({
  selector: 'app-select-seat',
  templateUrl: './select-seat.component.html',
  styleUrls: ['./select-seat.component.scss']
})
export class SelectSeatComponent implements OnInit {

  @Input() venue: Venue;
  @Input() performanceId: number;

  layout: LayoutUnitSelect[][];

  selectedSeats: LayoutUnitSelect[] = [];

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
  }

  addSeat(seat: LayoutUnitSelect): void {
    this.selectedSeats.push(seat);
  }

  removeSeat(seat: LayoutUnitSelect): void {
    const index = this.selectedSeats.indexOf(seat);
    if (index > -1) {
      this.selectedSeats.splice(index, 1);
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
