import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ApplicationEventService } from 'src/app/services/event.service';
import { ApplicationPerformanceService } from 'src/app/services/performance.service';
import {Performance} from '../../../dtos/performance';

@Component({
  selector: 'app-performance-search',
  templateUrl: './performance-search.component.html',
  styleUrls: ['./performance-search.component.scss']
})
export class PerformanceSearchComponent implements OnInit {

  @Output() searchedPerformances = new EventEmitter<Performance[]>();
  @Output() searchedNoPerformance = new EventEmitter<any>();

  // Error flag
  error = false;
  errorMessage = '';


  performanceSearchForm: FormGroup;
  performances = [];
  noPerformance = true;
  page = 0;
  size = 8;
  eventSelectPage = 0;
  eventSelectSize = 8;
  eventsSelect = [];

  constructor(private formBuilder: FormBuilder, private performanceService: ApplicationPerformanceService,
    private eventService: ApplicationEventService) { }

  ngOnInit(): void {
    this.performanceSearchForm = this.formBuilder.group({
      date: ['', []],
      price: ['', []],
      event: ['', []],
      venue: ['', []],
      time: ['', []],
    });

    //TODO maybe get from parent??
    this.eventService.getEvents(this.eventSelectPage, this.eventSelectSize).subscribe(
      (response) => {
        this.eventsSelect.push(...response);
      }, error => {
        console.error(error);
      }
    );
  }

  searchPerformances() {

    if(this.performanceSearchForm.value.date === '' && this.performanceSearchForm.value.price === ''
      && this.performanceSearchForm.value.time === '' && this.performanceSearchForm.value.event === ''
      && this.performanceSearchForm.value.venue === '') {

    this.performanceService.getPerformances(this.page, this.size).subscribe(
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
      }, error => {
        console.error(error);
      }
    );

      } else {

        let date = null;
        if (this.performanceSearchForm.value.date !== '') {
          if (this.performanceSearchForm.value.time !== '') {
            date = new Date(this.performanceSearchForm.value.date + 'T' + this.performanceSearchForm.value.time);
          } else {
            date = new Date(this.performanceSearchForm.value.date);
          }
        }

        this.performanceService.searchPerformances(this.page, this.size, date).subscribe(
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
          }, error => {
            console.error(error);
          }
        );
      }

  }

  resetValues() {
    this.performances = [];
    this.noPerformance = true;
    this.page = 0;
    this.size = 8;
  }

  loadMoreEventsForSelect(){
    this.eventService.getEvents(this.eventSelectPage, this.eventSelectSize).subscribe(
      (response) => {
        this.eventsSelect.push(...response);
      }, error => {
        console.error(error);
      }
    );
  }

}
