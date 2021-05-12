import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'ticketline-mockup';

  shoppingCart = [{name: 'test1', count: 1, price: 5.1}, {name: 'test2', count: 3, price: 10}, {name: 'test3', count: 10, price: 1.4}, {name: 'test4', count: 5000, price: 7.7}];
  cartToggle = true;
}
