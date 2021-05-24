import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ShoppingcartService } from 'src/app/services/shoppingcart.service';

@Component({
  selector: 'app-shoppingcart',
  templateUrl: './shoppingcart.component.html',
  styleUrls: ['./shoppingcart.component.scss']
})
export class ShoppingcartComponent implements OnInit {

  constructor(public cartService: ShoppingcartService) {}

  ngOnInit(): void {
  }

  close(): void {
    this.cartService.toggleStatus();
  }
}
