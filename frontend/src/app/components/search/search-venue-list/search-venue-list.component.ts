import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { VenueService } from 'src/app/services/venue.service';

@Component({
  selector: 'app-search-venue-list',
  templateUrl: './search-venue-list.component.html',
  styleUrls: ['./search-venue-list.component.scss']
})
export class SearchVenueListComponent implements OnInit {

  @Output() selectedVenue = new EventEmitter<any>();

  @Input() reset = false;

  venueSearchForm: FormGroup;
  venues = [];
  filteredVenues = [];

  constructor(private formBuilder: FormBuilder, private venueService: VenueService) { }

  ngOnInit(): void {
    this.venueSearchForm = this.formBuilder.group({
      venueName: ['', []],
      selectedVenue: ['', []],
    });

    this.venues = [];
    this.filteredVenues = [];
    this.getVenues();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.reset) {
      this.venueSearchForm = this.formBuilder.group({
        venueName: ['', []],
        selectedVenue: ['', []],
      });

      this.getVenues();
      this.reset = false;
    }
  }

  filterVenues() {
    const filterName = this.venueSearchForm.value.venueName;

    if (filterName !== '') {

      this.filteredVenues = this.venues.filter(
        (val) => {
          if (filterName !== '') {
            if (!val.name.includes(filterName)) {
              return false;
            }
            return true;
          }

        });

    } else {
      this.filteredVenues = this.venues;
    }
  }

  getVenues() {
    this.venueService.getVenues().subscribe(
      (response) => {
        console.log(response);

        this.venues.push(...response);
        this.filteredVenues.push(...response);


        if (this.venueSearchForm.value.venueName) {
          this.filterVenues();
        }
      }, error => {
        console.error(error);
      }
    );
  }

  venueSelected() {
    this.selectedVenue.emit(this.venueSearchForm.value.selectedVenue);
  }

}
