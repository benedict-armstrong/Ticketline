import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Address } from 'src/app/dtos/address';
import { LayoutUnit } from 'src/app/dtos/layoutUnit';
import { Sector } from 'src/app/dtos/sector';
import { Venue } from 'src/app/dtos/venue';
import { VenueService } from 'src/app/services/venue.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-add-venue',
  templateUrl: './add-venue.component.html',
  styleUrls: ['./add-venue.component.scss'],
})
export class AddVenueComponent implements OnInit {
  venueForm: FormGroup;
  submitted = false;
  error = false;
  success = false;
  errorMessage = '';

  sectors: Sector[] = [];
  venueLayout: LayoutUnit[][];

  constructor(
    private formBuilder: FormBuilder,
    private venueService: VenueService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.venueForm = this.formBuilder.group({
      venueName: ['', [Validators.required]],
      addressName: ['', [Validators.required]],
      lineOne: ['', [Validators.required]],
      lineTwo: [''],
      city: ['', [Validators.required]],
      postcode: ['', [Validators.required]],
      country: ['', [Validators.required]],
    });
  }

  updateSector(sectors: Sector[]) {
    this.sectors = sectors;
  }

  updateLayout(venueLayout: LayoutUnit[][]) {
    //console.log(venueLayout);
    this.venueLayout = venueLayout;
  }

  add() {
    this.submitted = true;
    if (this.venueForm.valid) {
      const venue: Venue = new Venue(
        null,
        this.venueForm.value.venueName,
        new Address(
          null,
          this.venueForm.value.addressName,
          this.venueForm.value.lineOne,
          this.venueForm.value.lineTwo,
          this.venueForm.value.city,
          this.venueForm.value.postcode,
          this.venueForm.value.country,
          true
        ),
        this.sectors,
        this.venueLayout
      );
      this.venueService.create(venue).subscribe(
        () => {
          this.success = true;
          setTimeout(() => {
            this.router.navigate(['/']);
          }, 3000);
        },
        (error) => {
          this.defaultServiceErrorHandling(error);
        }
      );
    } else {
      this.defaultServiceErrorHandling({error: 'Some values are not correct or missing'});
    }
  }

  vanishAlert() {
    this.error = false;
    this.errorMessage = '';
  }

  vanishSuccess() {
    this.success = false;
  }

  private defaultServiceErrorHandling(error: any) {
    //console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      //console.log('error');
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }
}
