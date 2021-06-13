import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Event } from '../../../dtos/event';
import { ApplicationEventService } from '../../../services/event.service';

@Component({
  selector: 'app-event-search',
  templateUrl: './event-search.component.html',
  styleUrls: ['./event-search.component.scss']
})
export class EventSearchComponent implements OnInit {

  @Output() searchedEvents = new EventEmitter<Event[]>();
  @Output() searchedDates = new EventEmitter<any>();
  @Output() searchedNoEvent = new EventEmitter<any>();
  @Output() searchPagination = new EventEmitter<any>();

  // Error flag
  error = false;
  errorMessage = '';

  eventSearchForm: FormGroup;
  page = 0;
  size = 8;
  events = [];
  dates = [];
  noEvent = true;

  constructor(private formBuilder: FormBuilder, private eventService: ApplicationEventService) { }

  ngOnInit(): void {

    this.eventSearchForm = this.formBuilder.group({
      title: ['', []],
      description: ['', []],
      duration: [null, []],
      eventType: ['', []],
    });
  }

  searchEvents() {

    // console.log(this.eventSearchForm.value);

    if (this.eventSearchForm.value.title === '' && this.eventSearchForm.value.description === ''
      && this.eventSearchForm.value.duration === null && this.eventSearchForm.value.eventType === '') {
      this.page = 0;
      this.size = 8;
      this.eventService.getEvents(this.page, this.size).subscribe(
        response => {
          console.log(response);

          // for (const event of response) {
          //   const date = new Date(event.date).toDateString();
          //
          //   if (!this.dates.includes(date)) {
          //     this.dates.push(date);
          //   }
          // }
          //
          // console.log(this.dates);

          this.events.push(...response);
          if (response.length < this.size) {
            this.noEvent = true;
          } else {
            this.page++;
            this.noEvent = false;
          }

          this.searchedEvents.emit(this.events);
          this.searchedDates.emit(this.dates);
          this.searchedNoEvent.emit(this.noEvent);
        }, error => {
          console.error(error);
        }
      );
    } else {
      this.eventService.searchEvents(this.page, this.size, this.eventSearchForm.value.title,
        this.eventSearchForm.value.description, this.eventSearchForm.value.duration, this.eventSearchForm.value.eventType).subscribe(
          response => {
            console.log(response);

            // for (const event of response) {
            //   const date = new Date(event.date).toDateString();
            //
            //   if (!this.dates.includes(date)) {
            //     this.dates.push(date);
            //   }
            // }
            // console.log(this.dates);
            this.events.push(...response);
            if (response.length < this.size) {
              this.noEvent = true;
            } else {
              this.page++;
              this.noEvent = false;
            }
            this.searchedEvents.emit(this.events);
            this.searchedDates.emit(this.dates);
            this.searchedNoEvent.emit(this.noEvent);
            this.searchPagination.emit(true);
          }, error => {
            console.error(error);
          }
        );
    }
  }

  resetValues() {
    this.events = [];
    this.dates = [];
    this.noEvent = true;
    this.page = 0;
    this.size = 8;
  }

  resetSearchFields(){
    this.eventSearchForm.reset();
    this.eventSearchForm.value.title = '';
    this.eventSearchForm.value.description = '';
    this.eventSearchForm.value.eventType = '';
    this.resetValues();
    this.searchEvents();
  }
}
