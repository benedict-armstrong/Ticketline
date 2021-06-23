import {Component, Input, OnInit} from '@angular/core';
import { NewTicket } from 'src/app/dtos/newTicket';
import { Sector } from 'src/app/dtos/sector';
import { Ticket } from 'src/app/dtos/ticket';
import { TicketService } from 'src/app/services/ticket.service';
import {LayoutUnit} from '../../../dtos/layoutUnit';

@Component({
  selector: 'app-select-seat-unit',
  templateUrl: './select-seat-unit.component.html',
  styleUrls: ['./select-seat-unit.component.scss']
})
export class SelectSeatUnitComponent implements OnInit {

  @Input()
  layoutUnit: LayoutUnit;
  @Input()
  performanceId: number;

  constructor(private ticketService: TicketService) { }

  ngOnInit(): void {
  }

  onClick(): void {
    if (this.layoutUnit === null) {
      return;
    }
    const sector: Sector = this.layoutUnit.sector as Sector;
    if (sector.type === 'STAGE') {
      return;
    }
    if (!this.layoutUnit.free) {
      return;
    }

    const addTicket: NewTicket = {
      performanceId: this.performanceId,
      ticketType: null,
      amount: null,
      seatId: this.layoutUnit.id
    };
    this.ticketService.addTicket(addTicket).subscribe(
      (responseTickets: Ticket[]) => {

        let done = false;
        for (let i = 0; i < this.ticketService.cart.length; i++) {
          if (this.ticketService.cart[i].length === 0) {
            this.ticketService.cart[i] = responseTickets;
            done = true;
            break;
          } else {
            if (this.ticketService.cart[i][0].performance.id === responseTickets[0].performance.id) {
              responseTickets.forEach(ticket => {
                this.ticketService.cart[i].push(ticket);
              });
              done = true;
              break;
            }
          }
        }

        if (!done) {
          this.ticketService.cart.push(responseTickets);
        }
        this.ticketService.updatePrice();
      },
      (error) => {
      }
    );
  }

}
