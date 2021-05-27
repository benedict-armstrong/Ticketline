import {Component, Input, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TicketService } from 'src/app/services/ticket.service';
import { TicketType } from '../../dtos/ticketType';
import { Performance } from '../../dtos/performance';

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

  error = false;
  errorMessage = '';
  success = false;

  constructor(
    private formBuilder: FormBuilder,
    private ticketService: TicketService
  ) {
    this.ticketForm = this.formBuilder.group({
      amount: [0, [Validators.min(1)]]
    });
  }

  incAmount(): void {
    this.ticketForm.setValue({amount: this.ticketForm.value.amount + 1});
    this.error = false;
  }

  decAmount(): void {
    if (this.ticketForm.value.amount > 0) {
      this.ticketForm.setValue({amount: this.ticketForm.value.amount - 1});
      this.error = false;
    }
  }

  addToCart(): void {
    if (this.ticketForm.valid) {
      this.error = false;
      this.ticketService.addToCart({
        id: null,
        performance: this.performance,
        ticketType: this.ticketType,
        seats: [this.ticketForm.value.amount],
        status: null
      });
    } else {
      this.error = true;
    }
  }

  ngOnInit(): void {
  }

}
