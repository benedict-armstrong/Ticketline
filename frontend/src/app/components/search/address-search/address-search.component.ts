import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {AddressService} from '../../../services/address.service';
import {Address} from '../../../dtos/address';

@Component({
  selector: 'app-address-search',
  templateUrl: './address-search.component.html',
  styleUrls: ['./address-search.component.scss']
})
export class AddressSearchComponent implements OnInit {

  @Output() searchedAddresses = new EventEmitter<Address[]>();
  @Output() searchedNoAddress = new EventEmitter<any>();

  // Error flag
  error = false;
  errorMessage = '';

  addressSearchForm: FormGroup;
  page = 0;
  size = 8;
  addresses = [];
  noAddress = true;

  constructor(private formBuilder: FormBuilder, private addressService: AddressService) { }

  ngOnInit(): void {

    this.addressSearchForm = this.formBuilder.group({
      name: ['', []],
      lineOne: ['', []],
      lineTwo: ['', []],
      city: ['', []],
      postCode: ['', []],
      country: ['', []],
    });
  }

  searchAddress() {
    if (this.noAddress && this.page !== 0) {
      return;
    }

    if (this.addressSearchForm.value.name === '' && this.addressSearchForm.value.lineOne === ''
      && this.addressSearchForm.value.lineTwo === '' && this.addressSearchForm.value.city === ''
      && this.addressSearchForm.value.postCode === '' && this.addressSearchForm.value.country === '') {
      this.addressService.getAddresses(this.page, this.size).subscribe(
        response => {
          this.addresses.push(...response);
          if (response.length < this.size) {
            this.noAddress = true;
          } else {
            this.page++;
            this.noAddress = false;
          }

          this.searchedAddresses.emit(this.addresses);
          this.searchedNoAddress.emit(this.noAddress);
        }, error => {
          console.error(error);
        }
      );
    } else {
      this.addressService.searchAddresses(this.page, this.size, this.addressSearchForm.value.name,
        this.addressSearchForm.value.lineOne, this.addressSearchForm.value.lineTwo, this.addressSearchForm.value.city,
        this.addressSearchForm.value.postCode, this.addressSearchForm.value.country).subscribe(
        response => {
          this.addresses.push(...response);
          if (response.length < this.size) {
            this.noAddress = true;
          } else {
            this.page++;
            this.noAddress = false;
          }
          this.searchedAddresses.emit(this.addresses);
          this.searchedNoAddress.emit(this.noAddress);
        }, error => {
          console.error(error);
        }
      );
    }
  }

  resetValues() {
    this.addresses = [];
    this.noAddress = true;
    this.page = 0;
    this.size = 8;
  }

  resetSearchFields() {
    this.addressSearchForm.reset();
    this.addressSearchForm.value.title = '';
    this.addressSearchForm.value.lineOne = '';
    this.addressSearchForm.value.lineTwo = '';
    this.addressSearchForm.value.city = '';
    this.addressSearchForm.value.postCode = '';
    this.addressSearchForm.value.country = '';
    this.resetValues();
    this.searchAddress();
  }
}
