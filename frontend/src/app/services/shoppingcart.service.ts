import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ShoppingcartService {

  cart = [{name: 'test1', count: 1, price: 5.1}, {name: 'test2', count: 3, price: 10}, {name: 'test3', count: 10, price: 1.4}, {name: 'test4', count: 5000, price: 7.7}];
  status = true;

  constructor() { }
}
