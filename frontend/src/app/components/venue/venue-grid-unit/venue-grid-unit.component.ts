import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";

import { GridTool } from "../models/gridTool";
import { SeatUnit } from "../models/seatUnit";

@Component({
  selector: "app-venue-grid-unit",
  templateUrl: "./venue-grid-unit.component.html",
  styleUrls: ["./venue-grid-unit.component.scss"],
})
export class VenueGridUnitComponent implements OnInit {
  @Input()
  seatUnit: SeatUnit;

  @Input()
  activeTool: GridTool;

  @Output()
  clickedUnit = new EventEmitter<SeatUnit>();

  constructor() {}

  ngOnInit(): void {}

  toggle() {
    if (this.activeTool.action == "assignSector") {
      if (this.activeTool.scope == "single") {
        this.seatUnit.sectorId = this.activeTool.sectorId;
      } else {
        this.toggleEvent();
      }
    } else {
      if (this.activeTool.scope == "single") {
        this.seatUnit.available = !this.seatUnit.available;
        this.seatUnit.sectorId = null;
      } else {
        this.toggleEvent();
      }
    }
  }

  toggleEvent(event?) {
    if ((event && (event.buttons == 1 || event.buttons == 3)) || !event) {
      if (this.activeTool.scope == "single") {
        if (this.activeTool.action == "add") {
          this.seatUnit.available = true;
        } else if (this.activeTool.action == "remove") {
          this.seatUnit.available = false;
          this.seatUnit.sectorId = null;
        } else if (this.activeTool.action == "assignSector") {
          this.seatUnit.sectorId = this.activeTool.sectorId;
        }
      } else {
        this.clickedUnit.emit(this.seatUnit);
      }
    }
  }
}
