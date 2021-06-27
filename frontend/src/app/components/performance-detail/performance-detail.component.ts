import { Component, OnInit } from '@angular/core';
import {Performance} from '../../dtos/performance';
import {ApplicationPerformanceService} from '../../services/performance.service';
import {ActivatedRoute} from '@angular/router';
import { TicketService } from 'src/app/services/ticket.service';
import { SeatCount } from 'src/app/dtos/seatCount';

@Component({
  selector: 'app-performance-detail',
  templateUrl: './performance-detail.component.html',
  styleUrls: ['./performance-detail.component.scss']
})
export class PerformanceDetailComponent implements OnInit {

  performance: Performance = null;
  imgURL = [];
  error = false;
  errorMessage = '';
  ticketCounts: SeatCount[] = [];
  selectSeats: boolean;

  constructor(private performanceService: ApplicationPerformanceService,
              private ticketService: TicketService,
              private activeRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.selectSeats = false;
    const performanceId = this.activeRoute.snapshot.params.id;
    this.getPerformance(performanceId);
    this.ticketService.cartState$.subscribe(
      () => {
        this.getPerformance(performanceId);
      }
    );
  }

  vanishAlert(): void {
    this.error = false;
  }

  selectSeatsFunc(value: boolean) {
    const performanceId = this.activeRoute.snapshot.params.id;
    this.performance = null;
    this.getPerformance(performanceId);
    this.selectSeats = value;
  }

  private getPerformance(performanceId) {
    this.performanceService.getPerformanceById(performanceId).subscribe(
      (response) => {
        if (this.performance === null) {
          this.performance = response;
        }

        this.ticketService.getSeatCounts(response.id).subscribe(
          (seatCounts: SeatCount[]) => {
            this.ticketCounts = [];
            for (const ticketType of response.ticketTypes) {
              for (const seatCount of seatCounts) {
                if (seatCount.sectorId === ticketType.sector.id) {
                  this.ticketCounts.push(seatCount);
                }
              }
            }
          },
          (error) => {
            this.defaultServiceErrorHandling(error);
          }
        );
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
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
