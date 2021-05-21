import { Component, OnInit } from '@angular/core';
import {EventService} from '../../services/event.service';
import {Event} from '../../dtos/event';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.scss']
})
export class EventsComponent implements OnInit {

  events: Event[] = [];
  dates = [];
  page = 0;
  size = 8;
  noEvent = true;

  constructor(private eventService: EventService) { }

  ngOnInit(): void {
    this.loadBatch();
  }

  /**
   * Loads the next news entries to be displayed.
   * The amount of news entries in one batch is specified in the property `limit`.
   * Offsetting is done with the help of IDs.
   */
  loadBatch() {
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
      }, error => {
        console.error(error);
      }
    );
  }

  // /**
  //  * get all Events for a certain date
  //  */
  // getEventsForDate(date: string): any[] {
  //   const eventsForDate = [];
  //   let isAfter = false;
  //   for (const event of this.events) {
  //     const eventDate = new Date(event.date);
  //     if (eventDate.toDateString() === date) {
  //       isAfter = true;
  //       eventsForDate.push(event);
  //       // ignore the events after the date
  //     } else if (isAfter) {
  //       return eventsForDate;
  //     }
  //   }
  //
  //   return eventsForDate;
  // }

}
