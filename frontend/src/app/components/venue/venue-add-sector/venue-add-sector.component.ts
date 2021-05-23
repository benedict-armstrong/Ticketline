import { Component, EventEmitter, OnInit, Output } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Sector } from "src/app/dtos/sector";
@Component({
  selector: "app-venue-add-sector",
  templateUrl: "./venue-add-sector.component.html",
  styleUrls: ["./venue-add-sector.component.scss"],
})
export class VenueAddSectorComponent implements OnInit {
  @Output() newSector = new EventEmitter<Sector[]>();

  venueAddSectorForm: FormGroup;
  sectors: Sector[] = [
    {
      name: "Stage",
      id: 0,
      description: "This is the Stage",
      color: "#CCCCCC",
      type: "STAGE",
    },
  ];
  submitted = false;

  colors: string[] = [
    "#CCCCCC",
    "#FF6633",
    "#B3B31A",
    "#FF33FF",
    "#FFFF99",
    "#00B3E6",
    "#E6B333",
    "#3366E6",
    "#999966",
    "#99FF99",
    "#B34D4D",
    "#991AFF",
  ];

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.venueAddSectorForm = this.formBuilder.group({
      name: ["", [Validators.required]],
      description: [""],
      type: ["SEATED", [Validators.required]],
    });
    this.newSector.emit(this.sectors);
  }

  createSector() {
    if (this.sectors.length > 9) {
      alert("Maximum of 10 sectors allowed");
      return;
    }
    this.submitted = true;
    if (this.venueAddSectorForm.valid) {
      this.submitted = false;
      this.sectors.push({
        name: this.venueAddSectorForm.value.name,
        description: this.venueAddSectorForm.value.description,
        type: this.venueAddSectorForm.value.type,
        id: this.sectors.length,
        color: this.colors[this.sectors.length],
      });
      this.venueAddSectorForm.reset();
      this.venueAddSectorForm.controls["type"].setValue("SEATED");
      this.newSector.emit(this.sectors);
    }
  }

  removeSector(sector: Sector) {
    if (this.sectors.length < 2) {
      alert("You must at least have one sector.");
      return;
    }
    const index = this.sectors.indexOf(sector);
    if (index > -1) {
      this.sectors.splice(index, 1);
      sector = null;
      this.newSector.emit(this.sectors);
    }
  }
}
