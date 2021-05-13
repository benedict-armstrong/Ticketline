import { Component, OnInit } from '@angular/core';
import {Event} from '../../dtos/event';
import {FileService} from '../../services/file.service';
import {ActivatedRoute} from '@angular/router';
import {ApplicationEventService} from '../../services/event.service';
import {AuthService} from '../../services/auth.service';

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

  constructor(private eventService: ApplicationEventService,
              private activeRoute: ActivatedRoute, private authService: AuthService) { }

  ngOnInit(): void {
    const eventId = this.activeRoute.snapshot.params.id;
    this.eventService.getEventById(eventId).subscribe(
      (response) => {
        this.eventItem = response;
        if (this.eventItem.images.length > 0) {
          for (let i = 0; i < this.eventItem.images.length; i++) {
            const img = FileService.asFile(this.eventItem.images[i].data, this.eventItem.images[i].type);
            this.setURL(img, i);
          }

          console.log(this.imgURL);
        }
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }

    );
  }

  vanishAlert(): void {
    this.error = false;
  }

  hasOrganizerPermission(): boolean {
    return this.authService.getUserRole() === 'ORGANIZER' || this.authService.getUserRole() === 'ADMIN';
  }

  private setURL(file: File, id: number) {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = _event => {
      this.imgURL[id] = reader.result;
    };
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
