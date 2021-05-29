import {Component, Input, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TicketService } from 'src/app/services/ticket.service';
import { TicketType } from '../../dtos/ticketType';
import { Performance } from '../../dtos/performance';
import { Ticket } from 'src/app/dtos/ticket';

@Component({
  selector: 'app-ticket-list-item',
  templateUrl: './ticket-list-item.component.html',
  styleUrls: ['./ticket-list-item.component.scss']
})
export class TicketListItemComponent implements OnInit {
  @Input()
  ticketType: TicketType;
  @Input()
  performance: Performance;

  ticketForm: FormGroup;

  public validationError = false;
  public error = false;
  public errorMessage = '';
  public success = false;
  public waiting = false;

  constructor(
    private formBuilder: FormBuilder,
    private ticketService: TicketService
  ) {
    this.ticketForm = this.formBuilder.group({
      amount: [0, [Validators.min(1)]]
    });
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
    if (!this.waiting) {
      if (this.ticketForm.valid) {
        this.validationError = false;
        const ticket: Ticket = {
          id: null,
          performance: this.performance,
          ticketType: this.ticketType,
          seats: [this.ticketForm.value.amount],
          status: null
        };
        this.waiting = true;
        this.ticketService.addTicket(ticket).subscribe(
          (responseTicket: Ticket) => {
            this.waiting = false;
            this.success = true;
            for (let i = 0; i < this.ticketService.cart.length; i++) {
              if (this.ticketService.cart[i].id === responseTicket.id
                || this.ticketService.cart[i].ticketType.id === responseTicket.ticketType.id) {
                this.ticketService.cart[i] = responseTicket;
                responseTicket = null;
                break;
              }
            }
            if (responseTicket != null) {
              this.ticketService.cart.push(responseTicket);
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

  ngOnInit(): void {
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
