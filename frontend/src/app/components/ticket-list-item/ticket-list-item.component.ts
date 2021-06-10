import {Component, Input, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CartItemService } from 'src/app/services/cartItem.service';
import { TicketType } from '../../dtos/ticketType';
import { Performance } from '../../dtos/performance';
import { Ticket } from 'src/app/dtos/ticket';
import { CartItem } from 'src/app/dtos/cartItem';
import { Sector } from 'src/app/dtos/sector';
import { NewCartItem } from 'src/app/dtos/newCartItem';

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

  constructor(
    private formBuilder: FormBuilder,
    private cartItemService: CartItemService
  ) {
    this.ticketForm = this.formBuilder.group({
      amount: [0, [Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    this.sector = this.ticketType.sector;
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
        const addTicket: NewCartItem = {
          performance: this.performance,
          ticketType: this.ticketType,
        };
        this.waiting = true;
        this.cartItemService.addCartItem(addTicket, this.ticketForm.value.amount).subscribe(
          (responseCartItem: CartItem) => {
            this.waiting = false;
            this.success = true;
            for (let i = 0; i < this.cartItemService.cart.length; i++) {
              if (this.cartItemService.cart[i].id === responseCartItem.id) {
                this.cartItemService.cart[i] = responseCartItem;
                responseCartItem = null;
                break;
              }
            }
            if (responseCartItem != null) {
              this.cartItemService.cart.push(responseCartItem);
            }
            this.cartItemService.updatePrice();
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
