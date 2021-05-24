import { Component, Input, OnInit } from '@angular/core';
import { GridTool } from '../models/gridTool';
import { SeatUnit } from '../models/seatUnit';
import { Sector } from 'src/app/dtos/sector';

@Component({
  selector: 'app-venue-layout-grid',
  templateUrl: './venue-layout-grid.component.html',
  styleUrls: ['./venue-layout-grid.component.scss'],
})
export class VenueLayoutGridComponent implements OnInit {
  @Input()
  venueLayout: SeatUnit[][];

  @Input()
  activeTool: GridTool;

  constructor() {}

  ngOnInit(): void {}

  seatUnitClicked(seatUnit: SeatUnit) {
    let found = false;
    for (const row of this.venueLayout) {
      const index = row.indexOf(seatUnit);
      if (index !== -1) {
        found = true;
        if (this.activeTool.scope === 'row') {
          if (this.activeTool.action === 'remove') {
            row.map((su) => ((su.available = false), (su.sector = null)));
          } else if (this.activeTool.action === 'add') {
            row.map((su) => (su.available = true));
          } else if (this.activeTool.action === 'assignSector') {
            row.map((su) => {
              if (su.available) {
                if (this.activeTool.reassign) {
                  su.sector = this.activeTool.sector;
                } else {
                  if (su.sector === null) {
                    su.sector = this.activeTool.sector;
                  }
                }
              }
            });
          }
        } else if (this.activeTool.scope === 'column') {
          found = true;
          for (const row2 of this.venueLayout) {
            if (this.activeTool.action === 'remove') {
              row2[index].available = false;
              row2[index].sector = null;
            } else if (this.activeTool.action === 'add') {
              row2[index].available = true;
            } else if (this.activeTool.action === 'assignSector') {
              if (row2[index].available) {
                if (this.activeTool.reassign) {
                  row2[index].sector = this.activeTool.sector;
                } else {
                  if (row2[index].sector === null) {
                    row2[index].sector = this.activeTool.sector;
                  }
                }
              }
            }
          }
        }
      }
      if (found) {
        break;
      }
    }
  }
}
