import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Ticket} from '../../../dtos/ticket';
import {Performance} from '../../../dtos/performance';

@Component({
  selector: 'app-cart-list',
  templateUrl: './cart-list.component.html',
  styleUrls: ['./cart-list.component.scss']
})
export class CartListComponent implements OnInit {

  @Input() ticket: Ticket;
  @Input() performance: Performance;

  @Output() updatedTicketType = new EventEmitter<Ticket>();

  constructor() { }

  ngOnInit(): void {
  }

  updateTicketType() {
    this.updatedTicketType.emit(this.ticket);
  }
}
