import { Component, OnInit } from '@angular/core';
import {Performance} from '../../dtos/performance';
import {ApplicationPerformanceService} from '../../services/performance.service';
import {ActivatedRoute} from '@angular/router';
import {Ticket} from '../../dtos/ticket';
import {TicketService} from '../../services/ticket.service';
import { User } from 'src/app/dtos/user';

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

  constructor(private performanceService: ApplicationPerformanceService,
              private activeRoute: ActivatedRoute,
              private ticketService: TicketService) { }

  ngOnInit(): void {
    const performanceId = this.activeRoute.snapshot.params.id;
    this.performanceService.getPerformanceById(performanceId).subscribe(
      (response) => {
        this.performance = response;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }

    );
  }

  vanishAlert(): void {
    this.error = false;
  }

  testtick() {
    const t = new Ticket(null, null, this.performance, this.performance.sectorTypes[0], [1],
      this.performance.ticketTypes[0], 100, 'PAID_FOR');
    this.ticketService.buy(t).subscribe(
      q => {
        console.log('SUCC', q);
      }, e => {
        console.log('ERR', e);
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
