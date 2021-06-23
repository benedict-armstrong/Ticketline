import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
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
  layoutUnit: LayoutUnitSelect;

  @Output() selectSeat = new EventEmitter<LayoutUnitSelect>();

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
