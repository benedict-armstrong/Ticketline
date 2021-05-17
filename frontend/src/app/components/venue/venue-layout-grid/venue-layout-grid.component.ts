import { Component, Input, OnInit } from "@angular/core";
import { GridTool } from "../models/gridTool";
import { SeatUnit } from "../models/seatUnit";

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
            this.venueLayout[i].map((su) => (su.available = false));
          } else if (this.activeTool.action == "add") {
            this.venueLayout[i].map((su) => (su.available = true));
          } else if (this.activeTool.action == "assignSector") {
            this.venueLayout[i].map(
              (su) => (su.sectorId = this.activeTool.sectorId)
            );
          }
        } else if (this.activeTool.scope == "column") {
          found = true;
          for (let j = 0; j < this.venueLayout.length; j++) {
            if (this.activeTool.action == "remove") {
              this.venueLayout[j][index].available = false;
            } else if (this.activeTool.action == "add") {
              this.venueLayout[j][index].available = true;
            } else if (this.activeTool.action == "assignSector") {
              this.venueLayout[j][index].sectorId = this.activeTool.sectorId;
            }
          }
        }
      }
      if (found) break;
    }
  }
}
