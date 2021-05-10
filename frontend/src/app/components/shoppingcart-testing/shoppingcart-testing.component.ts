import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';


@Component({
  selector: 'app-shoppingcart-testing',
  templateUrl: './shoppingcart-testing.component.html',
  styleUrls: ['./shoppingcart-testing.component.scss']
})
export class ShoppingcartTestingComponent implements OnInit {
  addCartForm: FormGroup;
  tickets = [{name: 'Normal', seating: 'Standing', age: 'Adult', maxNumber: 20, amount: 0}];

  constructor(
    private formBuilder: FormBuilder
  ) {
    this.addCartForm = this.formBuilder.group(
      this.tickets.reduce((map, obj) => {
        map[obj.name] = 0;
        return map;
      }, {})
    );
  }

  ngOnInit(): void {
  }

  addToCart(): void {
    console.log(this.addCartForm.value);
  }

}
