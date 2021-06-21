import { Component, OnInit } from '@angular/core';
import { TopEvent } from 'src/app/dtos/topEvent';
import {Event} from '../../dtos/event';
import {ApplicationEventService} from '../../services/event.service';

@Component({
  selector: 'app-explore',
  templateUrl: './explore.component.html',
  styleUrls: ['./explore.component.scss']
})
export class ExploreComponent implements OnInit {

  topEvents: TopEvent[];

  constructor(private eventService: ApplicationEventService) { }

  ngOnInit(): void {
    this.getEvents();
  }

  getEvents() {
    this.eventService.getTopEvents(0, 5).subscribe(
      (data) => this.topEvents = data
    );
  }

}
