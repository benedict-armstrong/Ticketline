import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VenueService} from '../../../services/venue.service';
import {Venue} from '../../../dtos/venue';

@Component({
  selector: 'app-select-venue',
  templateUrl: './select-venue.component.html',
  styleUrls: ['./select-venue.component.scss']
})
export class SelectVenueComponent implements OnInit {

  @Output() venueChanged = new EventEmitter<Venue>();
  selectVenueForm: FormGroup;
  venues: Venue[];
  filteredVenues: Venue[];
  submitted = false;

  constructor(private formBuilder: FormBuilder, private venueService: VenueService) {}

  ngOnInit(): void {

    this.selectVenueForm = this.formBuilder.group({
      filterName: [''],
      filterAddressName: [''],
      selectedVenue: ['']
    });

    this.venueService.getVenues().subscribe((response) => {
      this.venues = response;
      this.filteredVenues = this.venues;
    });
  }

  /**
   * filter the array of artists by lastname and firstname
   */
  filterVenues(): void {
    const filterName = this.selectVenueForm.value.filterName;
    const filterAddressName = this.selectVenueForm.value.filterAddressName;

    this.filteredVenues = this.venues.filter(
      (venue: Venue) => {
        if (filterName !== '') {
          if (!venue.name.includes(filterName)) {
            return false;
          }
        }

        if (filterAddressName !== '') {
          if (!venue.address.name.includes(filterAddressName)) {
            return false;
          }
        }

        return true;
      });
  }

  venueSelected(): void {
    this.venueChanged.emit(this.selectVenueForm.value.selectedVenue);
  }

}
