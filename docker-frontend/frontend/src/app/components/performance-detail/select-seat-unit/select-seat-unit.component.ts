import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { Ticket } from 'src/app/dtos/ticket';
import { TicketService } from 'src/app/services/ticket.service';
import {LayoutUnitSelect} from '../models/layoutUnitSelect';

@Component({
  selector: 'app-select-seat-unit',
  templateUrl: './select-seat-unit.component.html',
  styleUrls: ['./select-seat-unit.component.scss']
})
export class SelectSeatUnitComponent implements OnInit {

  @Input()
  layoutUnit: LayoutUnitSelect;

  @Input()
  performanceId: number;

  @Output() selectSeat = new EventEmitter<LayoutUnitSelect>();

  @Output() unSelectSeat = new EventEmitter<LayoutUnitSelect>();

  title: string;

  constructor(private ticketService: TicketService) { }

  ngOnInit(): void {
    if (this.layoutUnit) {
      this.initLayoutUnit();
      this.ticketService.cartState$.subscribe(
        (data) => {
          const tickets = [].concat(...data).filter((t: Ticket) => t.performance.id === this.performanceId);
          if (tickets.find((t: Ticket) => t.seat.id === this.layoutUnit.id)) {
            this.title = 'Already in your cart';
            this.layoutUnit.selected = true;
          } else {
            if (this.layoutUnit.selected) {
              this.layoutUnit.free = true;
            }
            this.layoutUnit.selected = false;
            this.setTitle();
          }
        }
      );
    }
  }

  select(): void {
    if (typeof this.layoutUnit.sector === 'object' && this.hasOwnProperty(this.layoutUnit.sector, 'type')) {
      if (this.layoutUnit.sector.type !== 'STAGE') {
        if (!this.layoutUnit.free && !this.layoutUnit.selected) {
          alert('This seat is booked already.');
        } else {
          if (!this.layoutUnit.selected) {
            this.selectSeat.emit(this.layoutUnit);
          } else {
            if (!this.layoutUnit.selected) {
              this.selectSeat.emit(this.layoutUnit);
            } else {
              this.unSelectSeat.emit(this.layoutUnit);
            }
          }
        }
      }
    }
  }

  // eslint-disable-next-line @typescript-eslint/ban-types
  private hasOwnProperty<X extends {}, Y extends PropertyKey>(obj: X, prop: Y): obj is X & Record<Y, unknown> {
    return obj.hasOwnProperty(prop);
  }

  private initLayoutUnit() {
    const tickets = [].concat(...this.ticketService.cart).filter((t: Ticket) => t.performance.id === this.performanceId);
    this.layoutUnit.selected = !!tickets.find((t: Ticket) => t.seat.id === this.layoutUnit.id);
    this.setTitle();
  }

  private setTitle() {
    if (typeof this.layoutUnit.sector === 'object' && this.hasOwnProperty(this.layoutUnit.sector, 'type')) {
      switch (this.layoutUnit.sector.type) {
        case 'STAGE':
          this.title = 'Stage';
          break;
        case 'SEATED':
          if (this.layoutUnit.free) {
            this.title = 'Seat: ' + this.layoutUnit.customLabel;
          } else if (this.layoutUnit.selected) {
            this.title = 'Already in your cart';
          } else {
            this.title = 'Booked';
          }
          break;
        case 'STANDING':
          if (this.layoutUnit.free) {
            this.title = 'Standing: ' + this.layoutUnit.customLabel;
          } else if (this.layoutUnit.selected) {
            this.title = 'Already in your cart';
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
