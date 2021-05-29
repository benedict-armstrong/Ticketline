import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { TicketService } from 'src/app/services/ticket.service';

@Component({
  selector: 'app-shoppingcart',
  templateUrl: './shoppingcart.component.html',
  styleUrls: ['./shoppingcart.component.scss']
})
export class ShoppingcartComponent implements OnInit {

  constructor(public ticketService: TicketService) {}

  ngOnInit(): void {
  }

  close(): void {
    this.ticketService.toggleStatus();
  }
}
