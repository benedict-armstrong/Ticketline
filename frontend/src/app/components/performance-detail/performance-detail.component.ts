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
