import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {Event} from '../../dtos/event';
import {EventService} from '../../services/event.service';

@Component({
  selector: 'app-event-detail',
  templateUrl: './event-detail.component.html',
  styleUrls: ['./event-detail.component.scss']
})
export class EventDetailComponent implements OnInit {

  eventItem: Event;
  imgURL = [];
  error = false;
  errorMessage = '';

  constructor(private eventService: EventService,
              private activeRoute: ActivatedRoute, private authService: AuthService) { }

  ngOnInit(): void {
    const eventId = this.activeRoute.snapshot.params.id;
    this.eventService.getEventById(eventId).subscribe(
      (response) => {
        this.eventItem = response;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }

    );
  }

  vanishAlert(): void {
    this.error = false;
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }

}
