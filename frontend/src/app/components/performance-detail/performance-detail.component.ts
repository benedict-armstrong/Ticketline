import { Component, OnInit } from '@angular/core';
import {Performance} from '../../dtos/performance';
import {ApplicationPerformanceService} from '../../services/performance.service';
import {ActivatedRoute} from '@angular/router';
import { Sector } from 'src/app/dtos/sector';
import { TicketType } from 'src/app/dtos/ticketType';
import { TicketService } from 'src/app/services/ticket.service';
import { SeatCount } from 'src/app/dtos/seatCount';

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
  ticketCounts: SeatCount[] = [];
  selectSeats: boolean;

  constructor(private performanceService: ApplicationPerformanceService,
              private ticketService: TicketService,
              private activeRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.selectSeats = false;
    const performanceId = this.activeRoute.snapshot.params.id;
    this.performanceService.getPerformanceById(performanceId).subscribe(
      (response) => {
        this.performance = response;

        this.ticketService.getSeatCounts(response.id).subscribe(
          (seatCounts: SeatCount[]) => {
            for (const seatCount of seatCounts) {
              for (const ticketType of response.ticketTypes) {
                if (seatCount.sectorId == ticketType.sector.id) {
                  this.ticketCounts.push(seatCount);
                }
              }
            }
          },
          (error) => {
            this.defaultServiceErrorHandling(error);
          }
        )
        // for (const row of response.venue.layout) {
        //   for (const cell of row) {
        //     for (let i = 0; i < response.ticketTypes.length; i++) {
        //       if (cell !== null) {
        //         if (cell.sector === response.ticketTypes[i].sector.id) {
        //           this.ticketCounts[i] += 1;
        //         }
        //       }
        //     }
        //   }
        //  }
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }

    );
  }

  vanishAlert(): void {
    this.error = false;
  }

  selectSeatsFunc(value: boolean) {
    this.selectSeats = value;
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
