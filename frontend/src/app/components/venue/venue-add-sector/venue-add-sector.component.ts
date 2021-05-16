import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { SeatUnit } from "../models/seatUnit";
import { Sector } from "../models/sector";
@Component({
  selector: "app-venue-add-sector",
  templateUrl: "./venue-add-sector.component.html",
  styleUrls: ["./venue-add-sector.component.scss"],
})
export class VenueAddSectorComponent implements OnInit {
  venueAddSectorForm: FormGroup;
  sectors: Sector[] = [];
  submitted = false;

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.venueAddSectorForm = this.formBuilder.group({
      name: ["", [Validators.required]],
      description: ["", [Validators.required]],
      type: ["seated", [Validators.required]],
    });
  }

  createSector() {
    this.submitted = true;
    if (this.venueAddSectorForm.valid) {
      this.submitted = false;
      this.sectors.push({
        name: this.venueAddSectorForm.value.name,
        description: this.venueAddSectorForm.value.description,
        type: this.venueAddSectorForm.value.type,
        sectorId: this.sectors.length + 1,
      });
      this.venueAddSectorForm.reset();
      this.venueAddSectorForm.controls["type"].setValue("seated");
    }
  }

  removeSector(sector: Sector) {
    const index = this.sectors.indexOf(sector);
    if (index > -1) {
      this.sectors.splice(index, 1);
    }
  }
}
