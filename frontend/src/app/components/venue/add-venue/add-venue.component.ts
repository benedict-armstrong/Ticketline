import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { Sector } from "../models/sector";

@Component({
  selector: "app-add-venue",
  templateUrl: "./add-venue.component.html",
  styleUrls: ["./add-venue.component.scss"],
})
export class AddVenueComponent implements OnInit {
  venueForm: FormGroup;
  submitted = false;

  sectors: Sector[] = [];

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.venueForm = this.formBuilder.group({
      venueName: ["", [Validators.required]],
      addressName: ["", [Validators.required]],
      lineOne: ["", [Validators.required]],
      lineTwo: [""],
      city: ["", [Validators.required]],
      postcode: ["", [Validators.required]],
      country: ["", [Validators.required]],
    });
  }

  updateSector(sectors: Sector[]) {
    this.sectors = sectors;
  }

  add() {}
}
