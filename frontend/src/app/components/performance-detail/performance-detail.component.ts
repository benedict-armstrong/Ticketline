import { Component, OnInit } from '@angular/core';
import {Performance} from '../../dtos/performance';
import {ApplicationPerformanceService} from '../../services/performance.service';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {FileService} from '../../services/file.service';

@Component({
  selector: 'app-performance-detail',
  templateUrl: './performance-detail.component.html',
  styleUrls: ['./performance-detail.component.scss']
})
export class PerformanceDetailComponent implements OnInit {

  eventItem: Performance;
  imgURL = [];
  error = false;
  errorMessage = '';

  constructor(private eventService: ApplicationPerformanceService,
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
