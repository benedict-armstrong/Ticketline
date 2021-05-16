import { Component, Input, OnInit } from "@angular/core";
import { SeatUnit } from "../models/seatUnit";

@Component({
  selector: "app-venue-grid-unit",
  templateUrl: "./venue-grid-unit.component.html",
  styleUrls: ["./venue-grid-unit.component.scss"],
})
export class VenueGridUnitComponent implements OnInit {
  @Input()
  seatUnit: SeatUnit;

  constructor() {}

  ngOnInit(): void {}

  toggle() {
    console.log(this.seatUnit);
    this.seatUnit.sectorId = 2;
    //this.seatUnit.available = !this.seatUnit.available;
  }
}
