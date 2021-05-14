import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { seatUnit } from "../models/seatUnit";
import { sector } from "../models/sector";
@Component({
  selector: "app-venue-add-sector",
  templateUrl: "./venue-add-sector.component.html",
  styleUrls: ["./venue-add-sector.component.scss"],
})
export class VenueAddSectorComponent implements OnInit {
  venueAddSectorForm: FormGroup;
  sectors: sector[] = [];

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.venueAddSectorForm = this.formBuilder.group({
      name: ["", [Validators.required]],
      description: ["", [Validators.required]],
      type: ["seated", [Validators.required]],
    });
  }

  createSector() {
    if (this.venueAddSectorForm.valid) {
      this.sectors.push({
        name: this.venueAddSectorForm.value.name,
        description: this.venueAddSectorForm.value.description,
        type: this.venueAddSectorForm.value.type,
        sectorId: this.sectors.length + 1,
      });
    }
  }
}
