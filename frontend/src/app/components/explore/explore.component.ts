import { Component, OnInit } from '@angular/core';
import {Event} from '../../dtos/event';
import {ApplicationEventService} from '../../services/event.service';

@Component({
  selector: 'app-explore',
  templateUrl: './explore.component.html',
  styleUrls: ['./explore.component.scss']
})
export class ExploreComponent implements OnInit {

  events: Event[];

  constructor(private eventService: ApplicationEventService) { }

  ngOnInit(): void {
    this.getEvents();
  }

  getEvents() {
    this.eventService.getEvents(0, 5).subscribe(
      (data) => this.events = data
    );
  }

}
