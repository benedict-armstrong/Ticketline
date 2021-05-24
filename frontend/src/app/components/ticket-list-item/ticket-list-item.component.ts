import {Component, Input, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ShoppingcartService } from 'src/app/services/shoppingcart.service';
import {SectorType} from '../../dtos/sectortype';

@Component({
  selector: 'app-ticket-list-item',
  templateUrl: './ticket-list-item.component.html',
  styleUrls: ['./ticket-list-item.component.scss']
})
export class TicketListItemComponent implements OnInit {
  @Input()
  sectorType: SectorType;

  ticketForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';

  // Success Flag
  success = false;

  price = 10;

  constructor(
    private formBuilder: FormBuilder,
    private cartService: ShoppingcartService
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
      this.cartService.addToCart({
        id: null,
        userId: 2,
        ticketId: this.sectorType.name.charCodeAt(1),
        amount: this.ticketForm.value.amount
      });
    } else {
      this.error = true;
    }
  }

  ngOnInit(): void {
  }

}
