import {Component, Input, OnInit} from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
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

  constructor(
    private formBuilder: FormBuilder,
    private cartService: ShoppingcartService
  ) {
    this.ticketForm = this.formBuilder.group({
      amount: 0
    });
  }

  incAmount(): void {
    this.ticketForm.setValue({amount: this.ticketForm.value.amount + 1});
  }

  decAmount(): void {
    this.ticketForm.setValue({amount: this.ticketForm.value.amount - 1});
  }

  addToCart(): void {
    this.cartService.addToCart({
      name: this.sectorType.name,
      amount: this.ticketForm.value.amount,
      price: 0.01
    });
  }

  ngOnInit(): void {
  }

}
