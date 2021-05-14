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

  constructor(private formBuilder: FormBuilder, private router: Router) {}

  ngOnInit(): void {}

  add() {}
}
