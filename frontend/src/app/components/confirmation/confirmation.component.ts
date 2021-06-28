import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../dtos/user';
import { TicketService } from '../../services/ticket.service';
import { Ticket } from '../../dtos/ticket';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-news-detail',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.scss']
})
export class ConfirmationComponent implements OnInit {

  error = false;
  errorMessage = '';
  user: User;
  tickets: Ticket[] = [];

  constructor(private ticketService: TicketService,
              private userService: UserService,
              private route: Router,
              private actRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    const userId = this.actRoute.snapshot.params.userId;
    const performanceId = this.actRoute.snapshot.params.perfId;
    this.userService.getUserByIdForConfirmation(userId).subscribe(
      (response) => {
        this.user = response;
        this.user.lastName = this.user.lastName.charAt(0) + '.';
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }

    );
    this.ticketService.confirmation(userId, performanceId).subscribe(
      (response) => {
        this.tickets = response;
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
    //console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }
}
