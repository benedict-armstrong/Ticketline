import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ApplicationEventService } from 'src/app/services/event.service';
import { ApplicationPerformanceService } from 'src/app/services/performance.service';
import { Performance } from '../../../dtos/performance';
import { Event } from '../../../dtos/event';
import { Venue } from 'src/app/dtos/venue';

@Component({
  selector: 'app-performance-search',
  templateUrl: './performance-search.component.html',
  styleUrls: ['./performance-search.component.scss']
})
export class PerformanceSearchComponent implements OnInit {

  @Output() searchedPerformances = new EventEmitter<Performance[]>();
  @Output() searchedNoPerformance = new EventEmitter<any>();
  @Output() searchedEventPerformance = new EventEmitter<any>();
  @Output() searchedVenuePerformance = new EventEmitter<any>();

  // Error flag
  error = false;
  errorMessage = '';


  performanceSearchForm: FormGroup;
  performances = [];
  noPerformance = true;
  page = 0;
  size = 8;
  selectedEvent = null;
  selectedVenue = null;
  doDateSearch = true;
  resetVenue = false;
  resetEvent = false;

  constructor(private formBuilder: FormBuilder, private performanceService: ApplicationPerformanceService,
    private eventService: ApplicationEventService) { }

  ngOnInit(): void {
    this.performanceSearchForm = this.formBuilder.group({
      date: ['', []],
      time: ['', []],
    });

  }

  searchPerformances() {

    if (this.performanceSearchForm.value.date === ''
      && this.performanceSearchForm.value.time === '' && this.performanceSearchForm.value.event === ''
      && this.performanceSearchForm.value.venue === '') {

      this.performanceService.getPerformances(this.page, this.size).subscribe(
        response => {
          this.performances.push(...response);

          if (response.length < this.size) {
            this.noPerformance = true;
          } else {
            this.page++;
            this.noPerformance = false;
          }
          this.searchedPerformances.emit(this.performances);
          this.searchedNoPerformance.emit(this.noPerformance);
        }, error => {
          console.error(error);
        }
      );

    } else {
      this.doDateSearch = true;

      let date = null;
      if (this.performanceSearchForm.value.date !== '' && this.performanceSearchForm.value.date !== null) {

        if (this.performanceSearchForm.value.time !== '' && this.performanceSearchForm.value.time !== null) {
          date = new Date(this.performanceSearchForm.value.date + 'T' + this.performanceSearchForm.value.time);
        } else {
          date = new Date(this.performanceSearchForm.value.date);
        }
      } else {
        if (this.performanceSearchForm.value.time !== '') {
          this.doDateSearch = false;
        }
      }

      let eventId;

      if (this.selectedEvent) {
        eventId = this.selectedEvent.id;
      } else {
        eventId = null;
      }

      let venueId;

      if (this.selectedVenue) {
        venueId = this.selectedVenue.id;
      } else {
        venueId = null;
      }

      if (this.doDateSearch) {
        this.performanceService.searchPerformances(this.page, this.size, date, eventId, venueId).subscribe(
            response => {
              console.log(response);
              this.performances.push(...response);

              if (response.length < this.size) {
                this.noPerformance = true;
              } else {
                this.page++;
                this.noPerformance = false;
              }

              this.searchedPerformances.emit(this.performances);
              this.searchedNoPerformance.emit(this.noPerformance);
              if (this.selectedEvent) {
                this.searchedEventPerformance.emit(this.selectedEvent);
              } else {
                this.searchedEventPerformance.emit(undefined);
              }
              if (this.selectedVenue) {
                this.searchedVenuePerformance.emit(this.selectedVenue);
              } else {
                this.searchedVenuePerformance.emit(undefined);
              }
            }, error => {
              console.error(error);
            }
          );
      }
    }
  }

  resetValues() {
    this.performances = [];
    this.noPerformance = true;
    this.page = 0;
    this.size = 8;
  }

  setSelectedEvent(event: Event) {
    this.selectedEvent = event[0];
  }

  setSelectedVenue(venue: Venue) {
    this.selectedVenue = venue[0];
  }

  resetSearchFields() {
    this.performanceSearchForm.reset();
    this.performanceSearchForm.value.date = '';
    this.performanceSearchForm.value.time = '';
    this.selectedEvent = null;
    this.selectedVenue = null;
    this.resetVenue = true;
    this.resetEvent = true;
    this.searchPerformances();
  }
}
