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
    this.getPerformance();
    this.ticketService.cartState$.subscribe(
      () => {
        this.getPerformance();
      }
    );
  }

  vanishAlert(): void {
    this.error = false;
  }

  selectSeatsFunc(value: boolean) {
    this.selectSeats = value;
  }

  private getPerformance() {
    const performanceId = this.activeRoute.snapshot.params.id;
    this.performanceService.getPerformanceById(performanceId).subscribe(
      (response) => {
        if (this.performance === null || this.performance === undefined) {
          this.performance = response;
        } else {
          this.performance.artist = response.artist;
          this.performance.date = response.date;
          this.performance.description = response.description;
          this.performance.id = response.id;
          this.performance.ticketTypes = response.ticketTypes;
          this.performance.title = response.title;
          this.performance.venue = response.venue;
        }

        this.performance.ticketTypes.sort((a, b) => a.price - b.price);
        this.performance.ticketTypes.sort((a, b) => a.sector.id - b.sector.id);

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
