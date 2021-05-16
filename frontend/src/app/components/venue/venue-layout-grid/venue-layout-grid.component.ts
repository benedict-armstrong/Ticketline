import { Component, Input, OnInit } from "@angular/core";
import { SeatUnit } from "../models/seatUnit";

@Component({
  selector: "app-venue-layout-grid",
  templateUrl: "./venue-layout-grid.component.html",
  styleUrls: ["./venue-layout-grid.component.scss"],
})
export class VenueLayoutGridComponent implements OnInit {
  @Input()
  venueLayout: SeatUnit[][];

  constructor() {}

  ngOnInit(): void {}
}
