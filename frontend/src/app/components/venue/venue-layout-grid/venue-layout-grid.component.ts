import { Component, Input, OnInit } from "@angular/core";
import { GridTool } from "../models/gridTool";
import { SeatUnit } from "../models/seatUnit";
import { Sector } from "../models/sector";

@Component({
  selector: "app-venue-layout-grid",
  templateUrl: "./venue-layout-grid.component.html",
  styleUrls: ["./venue-layout-grid.component.scss"],
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
    for (let i = 0; i < this.venueLayout.length; i++) {
      let index = this.venueLayout[i].indexOf(seatUnit);
      if (index != -1) {
        found = true;
        if (this.activeTool.scope == "row") {
          if (this.activeTool.action == "remove") {
            this.venueLayout[i].map(
              (su) => ((su.available = false), (su.sector = null))
            );
          } else if (this.activeTool.action == "add") {
            this.venueLayout[i].map((su) => (su.available = true));
          } else if (this.activeTool.action == "assignSector") {
            this.venueLayout[i].map((su) => {
              if (su.available) {
                if (this.activeTool.reassign) {
                  su.sector = this.activeTool.sector;
                } else {
                  if (su.sector == null) {
                    su.sector = this.activeTool.sector;
                  }
                }
              }
            });
          }
        } else if (this.activeTool.scope == "column") {
          found = true;
          for (let j = 0; j < this.venueLayout.length; j++) {
            if (this.activeTool.action == "remove") {
              this.venueLayout[j][index].available = false;
              this.venueLayout[j][index].sector = null;
            } else if (this.activeTool.action == "add") {
              this.venueLayout[j][index].available = true;
            } else if (this.activeTool.action == "assignSector") {
              if (this.venueLayout[j][index].available) {
                if (this.activeTool.reassign) {
                  this.venueLayout[j][index].sector = this.activeTool.sector;
                } else {
                  if (this.venueLayout[j][index].sector == null) {
                    this.venueLayout[j][index].sector = this.activeTool.sector;
                  }
                }
              }
            }
          }
        }
      }
      if (found) break;
    }
  }
}
