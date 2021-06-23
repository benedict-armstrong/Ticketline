import {Component, Input, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TicketService } from 'src/app/services/ticket.service';
import { TicketType } from '../../dtos/ticketType';
import { Performance } from '../../dtos/performance';
import { Ticket } from 'src/app/dtos/ticket';
import { Sector } from 'src/app/dtos/sector';
import { NewTicket } from 'src/app/dtos/newTicket';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-ticket-list-item',
  templateUrl: './ticket-list-item.component.html',
  styleUrls: ['./ticket-list-item.component.scss'],
})
export class TicketListItemComponent implements OnInit {
  @Input()
  performance: Performance;
  @Input()
  ticketCount: number;
  @Input()
  ticketType: TicketType;

  ticketForm: FormGroup;

  public sector: Sector;

  public validationError = false;
  public error = false;
  public errorMessage = '';
  public success = false;
  public waiting = false;
  public loggedIn = false;

  constructor(
    private formBuilder: FormBuilder,
    private ticketService: TicketService,
    private authService: AuthService
  ) {
    this.ticketForm = this.formBuilder.group({
      amount: [0, [Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.sector = this.ticketType.sector;
    this.loggedIn = this.authService.isLoggedIn();
  }

  incAmount(): void {
    this.vanishAlert();
    this.ticketForm.setValue({amount: this.ticketForm.value.amount + 1});
    this.validationError = false;
  }

  decAmount(): void {
    this.vanishAlert();
    if (this.ticketForm.value.amount > 0) {
      this.ticketForm.setValue({amount: this.ticketForm.value.amount - 1});
      this.validationError = false;
    } else {
      this.validationError = true;
    }
  }

  addToCart(): void {
    this.vanishAlert();
    if (!this.loggedIn) {
      this.error = true;
      const error = {
        error: {
          error: 'You are not logged in.'
        }
      };
      this.defaultServiceErrorHandling(error);
    }
    if (!this.waiting) {
      if (this.ticketForm.valid) {
        this.validationError = false;
        const addTicket: NewTicket = {
          performanceId: this.performance.id,
          ticketType: this.ticketType,
          amount: this.ticketForm.value.amount,
          seatId: null
        };
        this.waiting = true;
        this.ticketService.addTicket(addTicket).subscribe(
          (responseTickets: Ticket[]) => {
            this.waiting = false;
            this.success = true;

            let done = false;
            for (let i = 0; i < this.ticketService.cart.length; i++) {
              if (this.ticketService.cart[i].length === 0) {
                this.ticketService.cart[i] = responseTickets;
                done = true;
                break;
              } else {
                if (this.ticketService.cart[i][0].performance.id === responseTickets[0].performance.id) {
                  responseTickets.forEach(ticket => {
                    this.ticketService.cart[i].push(ticket);
                  });
                  done = true;
                  break;
                }
              }
            }

            if (!done) {
              this.ticketService.cart.push(responseTickets);
            }
            this.ticketService.updatePrice();
          },
          (error) => {
            this.waiting = false;
            this.error = true;
            this.defaultServiceErrorHandling(error);
          }
        );
      } else {
        this.validationError = true;
      }
    }
  }

  vanishAlert(): void {
    this.error = false;
    this.success = false;
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
