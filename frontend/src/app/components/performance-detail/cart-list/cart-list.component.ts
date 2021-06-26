import {Component, Input, OnInit} from '@angular/core';
import {Ticket} from '../../../dtos/ticket';
import {Performance} from '../../../dtos/performance';

@Component({
  selector: 'app-cart-list',
  templateUrl: './cart-list.component.html',
  styleUrls: ['./cart-list.component.scss']
})
export class CartListComponent implements OnInit {

  @Input() tickets: Ticket[];
  @Input() performance: Performance;

  constructor() { }

  ngOnInit(): void {
  }

}
