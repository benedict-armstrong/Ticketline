import {Component, Input, OnInit} from '@angular/core';
import {Address} from '../../../dtos/address';

@Component({
  selector: 'app-address-list-item',
  templateUrl: './address-list-item.component.html',
  styleUrls: ['./address-list-item.component.scss']
})
export class AddressListItemComponent implements OnInit {
  @Input() address: Address;

  constructor() { }

  ngOnInit(): void {
  }

}
