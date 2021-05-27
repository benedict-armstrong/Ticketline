import { Component, OnInit } from '@angular/core';
import {ApplicationEventService} from '../../../services/event.service';
import {Event} from '../../../dtos/event';
import {Artist} from '../../../dtos/artist';
import {Address} from '../../../dtos/address';
import {Performance} from '../../../dtos/performance';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.scss']
})
export class SearchResultComponent implements OnInit {

  events: Event[] = [];
  artists: Artist[] = [];
  addresses: Address[] = [];
  performances: Performance[] = [];
  dates = [];
  page = 0;
  size = 8;
  noEvent = true;
  noArtist = true;
  noAddress = true;
  noPerformance = true;
  eventSearched = true;
  artistSearched = false;
  addressSearched = false;
  performanceSearched = false;
  search = false;

  constructor(private eventService: ApplicationEventService) { }

  ngOnInit(): void {
    this.loadBatch();
  }

  /**
   * Loads the next e entries to be displayed.
   * The amount of news entries in one batch is specified in the property `limit`.
   * Offsetting is done with the help of IDs.
   */
  loadBatch() {
    this.eventService.getEvents(this.page, this.size).subscribe(
      response => {
        this.events.push(...response);
        if (response.length < this.size) {
          this.noEvent = true;
        } else {
          this.page++;
          this.noEvent = false;
        }
      }, error => {
        console.error(error);
      }
    );
  }

  setSearchEvents(results: Event[]) {
    this.eventSearched = true;
    this.artistSearched = false;
    this.addressSearched = false;
    this.performanceSearched = false;
    this.events = Object.assign([], results);
  }

  setSearchDates(dates: string[]) {
    this.dates = Object.assign([], dates);
  }

  setNoEvents(noEvent: boolean) {
    this.noEvent = noEvent;
  }

  setSearchPagination(search: boolean) {
    this.search = search;
  }

  setSearchArtists(results: Artist[]) {
    this.eventSearched = false;
    this.addressSearched = false;
    this.performanceSearched = false;
    this.artistSearched = true;
    this.artists = Object.assign([], results);
  }

  setNoArtist(noArtist: boolean) {
    this.noArtist = noArtist;
  }

  setSearchAddresses(results: Address[]) {
    this.eventSearched = false;
    this.artistSearched = false;
    this.performanceSearched = false;
    this.addressSearched = true;
    this.addresses = Object.assign([], results);
  }

  setNoAddress(noAddress: boolean) {
    this.noAddress = noAddress;
  }

  setSearchPerformances(results: Performance[]){
    this.eventSearched = false;
    this.artistSearched = false;
    this.addressSearched = false;
    this.performanceSearched = true;
    this.performances = Object.assign([], results);
  }

  setNoPerformance(noPerformance: boolean){
    this.noPerformance = noPerformance;
  }

  triggerSearchBtn(name: string){
    if (document.getElementById(name)) {
      if (document.getElementById(name).classList[1] === 'secondary') {
        document.getElementById(name).classList.remove('secondary');
        document.getElementById(name).classList.add('primary');
      } else {
        document.getElementById(name).classList.remove('primary');
        document.getElementById(name).classList.add('secondary');
      }
    }
  }
}
