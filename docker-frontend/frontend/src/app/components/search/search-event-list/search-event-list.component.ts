import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ApplicationEventService } from 'src/app/services/event.service';
import {Event} from '../../../dtos/event';

@Component({
  selector: 'app-search-event-list',
  templateUrl: './search-event-list.component.html',
  styleUrls: ['./search-event-list.component.scss']
})
export class SearchEventListComponent implements OnInit, OnChanges {

  @Output() selectedEvent = new EventEmitter<any>();

  @Input() reset = false;

  eventSearchForm: FormGroup;
  events = [];
  filteredEvents = [];
  page = 0;
  size = 8;
  noEvent = true;

  constructor(private formBuilder: FormBuilder, private eventService: ApplicationEventService) { }

  ngOnInit(): void {
    this.eventSearchForm = this.formBuilder.group({
      eventName: ['', []],
      selectedEvent: ['', []],
    });

    this.getEvents();

  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.reset) {
      this.eventSearchForm = this.formBuilder.group({
        eventName: ['', []],
        selectedEvent: ['', []],
      });

      this.resetValues();
      this.getEvents();
      this.reset = false;
    }
  }

  filterEvents(){
    const filterName = this.eventSearchForm.value.eventName;

    if(filterName !== ''){

    this.filteredEvents = this.events.filter(
      (val) => {
        if (filterName !== '') {
          if (!val.name.includes(filterName)) {
            return false;
          }
          return true;
        }

    });

  }else{
    this.filteredEvents = this.events;
  }
  }

  resetValues(){
    this.events = [];
    this.page=0;
    this.size=8;
  }

  loadMoreEvents() {
      this.getEvents();
  }

  getEvents(){
    this.eventService.getEvents(this.page, this.size).subscribe(
      (response) => {
        //console.log(response);

        this.events.push(...response);
        this.filteredEvents.push(...response);
        if (response.length < this.size) {
          this.noEvent = true;
        } else {
          this.page++;
          this.noEvent = false;
        }

        if (this.eventSearchForm.value.eventName) {
          this.filterEvents();
        }
      }, error => {
        //console.error(error);
      }
    );
  }

  eventSelected(){
    this.selectedEvent.emit(this.eventSearchForm.value.selectedEvent);
  }

}
