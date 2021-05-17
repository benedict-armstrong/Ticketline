import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { GridTool } from "../models/gridTool";
import { SeatUnit } from "../models/seatUnit";
import { Sector } from "../models/sector";

@Component({
  selector: "app-venue-creator",
  templateUrl: "./venue-creator.component.html",
  styleUrls: ["./venue-creator.component.scss"],
})
export class VenueCreatorComponent implements OnInit {
  venueLayoutForm: FormGroup;
  submitted = false;
  venueLayout: SeatUnit[][];
  sectors: Sector[];

  activeTool: GridTool = {
    action: "add",
    scope: "single",
    sectorId: 0,
  };

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.venueLayoutForm = this.formBuilder.group({
      gridSizeX: [
        20,
        [Validators.required, Validators.min(10), Validators.max(50)],
      ],
      gridSizeY: [
        20,
        [Validators.required, Validators.min(10), Validators.max(50)],
      ],
    });

    this.makeGrid();
  }

  makeGrid(): void {
    if (this.venueLayoutForm.valid) {
      this.activeTool.action = "add";

      this.venueLayout = new Array(this.venueLayoutForm.value.gridSizeX);

      for (let i = 0; i < this.venueLayoutForm.value.gridSizeX; i++) {
        this.venueLayout[i] = new Array(this.venueLayoutForm.value.gridSizeY);
        for (let j = 0; j < this.venueLayoutForm.value.gridSizeY; j++) {
          this.venueLayout[i][j] = {
            sectorId: null,
            type: "seating",
            available: true,
          };
        }
      }
    }
  }

  toggleAdd() {
    this.activeTool.action = "add";
  }

  toggleRemove() {
    this.activeTool.action = "remove";
  }

  toggleSingle() {
    this.activeTool.scope = "single";
  }

  toggleColumn() {
    this.activeTool.scope = "column";
  }

  toggleRow() {
    this.activeTool.scope = "row";
  }

  toggleSector() {
    this.activeTool.action = "assignSector";
  }
}
