import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-shoppingcart',
  templateUrl: './shoppingcart.component.html',
  styleUrls: ['./shoppingcart.component.scss']
})
export class ShoppingcartComponent implements OnInit {
  @Input() shoppingCart;

  constructor() { }

  ngOnInit(): void {
  }

}
