import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Component, Input, OnInit} from '@angular/core';
import { NewTicket } from 'src/app/dtos/newTicket';
import { Sector } from 'src/app/dtos/sector';
import { Ticket } from 'src/app/dtos/ticket';
import { TicketService } from 'src/app/services/ticket.service';
import {LayoutUnit} from '../../../dtos/layoutUnit';
import {Sector} from '../../../dtos/sector';
import {hasProperties} from 'codelyzer/util/astQuery';
import {LayoutUnitSelect} from '../models/layoutUnitSelect';

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
  layoutUnit: LayoutUnitSelect;

  @Output() selectSeat = new EventEmitter<LayoutUnitSelect>();

  constructor(private ticketService: TicketService) { }
  @Output() unSelectSeat = new EventEmitter<LayoutUnitSelect>();

  title: string;

  constructor() {
  }

  ngOnInit(): void {
    if (this.layoutUnit) {
      if (typeof this.layoutUnit.sector === 'object' && this.hasOwnProperty(this.layoutUnit.sector, 'type')) {
        switch (this.layoutUnit.sector.type) {
          case 'STAGE':
            this.title = 'Stage';
            break;
          case 'SEATED':
            if (this.layoutUnit.free) {
              this.title = 'Seat: ' + this.layoutUnit.customLabel;
            } else {
              this.title = 'Booked';
            }
            break;
          case 'STANDING':
            if (this.layoutUnit.free) {
              this.title = 'Standing: ' + this.layoutUnit.customLabel;
            } else {
              this.title = 'Booked';
            }
            break;
          default:
            this.title = 'Not available';
        }
      }
    }
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

  // eslint-disable-next-line @typescript-eslint/ban-types
  hasOwnProperty<X extends {}, Y extends PropertyKey>(obj: X, prop: Y): obj is X & Record<Y, unknown> {
    return obj.hasOwnProperty(prop);
  }

  select(): void {
    if (typeof this.layoutUnit.sector === 'object' && this.hasOwnProperty(this.layoutUnit.sector, 'type')) {
      if (this.layoutUnit.sector.type !== 'STAGE') {
        if (!this.layoutUnit.free) {
          alert('This seat is booked already.');
        } else {
          if (!this.layoutUnit.selected) {
            this.layoutUnit.selected = true;
            this.selectSeat.emit(this.layoutUnit);
          } else {
            if (!this.layoutUnit.selected) {
              this.selectSeat.emit(this.layoutUnit);
            } else {
              this.unSelectSeat.emit(this.layoutUnit);
            }
            this.layoutUnit.selected = !this.layoutUnit.selected;
          }
        }
      }
    }
  }
}
