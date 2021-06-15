import {Component, Input, OnInit} from '@angular/core';
import {Venue} from '../../../dtos/venue';

@Component({
  selector: 'app-select-seat',
  templateUrl: './select-seat.component.html',
  styleUrls: ['./select-seat.component.scss']
})
export class SelectSeatComponent implements OnInit {

  @Input() venue: Venue;

  constructor() { }

  ngOnInit(): void {
    for (const row of this.venue.layout) {
      for (const unit of row) {
        if (unit) {
          unit.sector = this.venue.sectors.find(s => s.id === unit.sector);
        }
      }
    }
  }

}
