import { Component, OnInit } from '@angular/core';
import {Performance} from '../../dtos/performance';
import {ApplicationPerformanceService} from '../../services/performance.service';
import {ActivatedRoute} from '@angular/router';
import { Sector } from 'src/app/dtos/sector';
import { TicketType } from 'src/app/dtos/ticketType';

@Component({
  selector: 'app-performance-detail',
  templateUrl: './performance-detail.component.html',
  styleUrls: ['./performance-detail.component.scss']
})
export class PerformanceDetailComponent implements OnInit {

  performance: Performance;
  imgURL = [];
  error = false;
  errorMessage = '';
  ticketCounts: number[] = [];

  constructor(private performanceService: ApplicationPerformanceService,
              private activeRoute: ActivatedRoute) { }

  ngOnInit(): void {
    const performanceId = this.activeRoute.snapshot.params.id;
    this.performanceService.getPerformanceById(performanceId).subscribe(
      (response) => {
        this.performance = response;

        response.ticketTypes.forEach(() => {
          this.ticketCounts.push(0);
        });

        console.log(response);

        for (const row of response.venue.layout) {
          for (const cell of row) {
            for (let i = 0; i < response.ticketTypes.length; i++) {
              if (cell !== null) {
                const sector: any = cell.sector;
                const sector2: Sector = sector;
                if (sector2.id === response.ticketTypes[i].sector.id) {
                  this.ticketCounts[i] += 1;
                }
              }
            }
          }
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
