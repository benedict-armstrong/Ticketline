import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";

@Component({
  selector: "app-add-venue",
  templateUrl: "./add-venue.component.html",
  styleUrls: ["./add-venue.component.scss"],
})
export class AddVenueComponent implements OnInit {
  venueForm: FormGroup;
  submitted = false;

  constructor(private formBuilder: FormBuilder, private router: Router) {}

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

  add() {}
}
