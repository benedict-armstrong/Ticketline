import {Component, Input, OnInit} from '@angular/core';
import {Venue} from '../../../dtos/venue';
import {LayoutUnitSelect} from '../models/layoutUnitSelect';

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

  constructor() {
  }

  ngOnInit(): void {
    for (const row of this.venue.layout) {
      for (const unit of row) {
        if (unit) {
          unit.sector = this.venue.sectors.find(s => s.id === unit.sector);
        }
      }
    }

    this.layout = this.venue.layout;
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
}
