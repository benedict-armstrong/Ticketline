import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { seatUnit } from "../models/seatUnit";
import { sector } from "../models/sector";

@Component({
  selector: "app-venue-creator",
  templateUrl: "./venue-creator.component.html",
  styleUrls: ["./venue-creator.component.scss"],
})
export class VenueCreatorComponent implements OnInit {
  venueLayoutForm: FormGroup;
  submitted = false;
  venueLayout: seatUnit[][];
  sectors: sector[];

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.venueLayoutForm = this.formBuilder.group({
      gridSizeX: [
        50,
        [Validators.required, Validators.min(10), Validators.max(100)],
      ],
      gridSizeY: [
        50,
        [Validators.required, Validators.min(10), Validators.max(100)],
      ],
    });

    this.makeGrid();
  }

  makeGrid(): void {
    if (this.venueLayoutForm.valid) {
      this.venueLayout = new Array(this.venueLayoutForm.value.gridSizeX).fill(
        new Array(this.venueLayoutForm.value.gridSizeY).fill({
          sectorId: 0,
          type: "seating",
        })
      );
    }
  }
}
