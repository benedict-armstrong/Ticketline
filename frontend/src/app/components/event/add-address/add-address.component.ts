import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AddressService} from '../../../services/address.service';
import {Address} from '../../../dtos/address';

@Component({
  selector: 'app-add-address',
  templateUrl: './add-address.component.html',
  styleUrls: ['./add-address.component.scss']
})
export class AddAddressComponent implements OnInit {
  @Output() locationChanged = new EventEmitter<Address>();
  addAddressForm: FormGroup;
  selectAddressForm: FormGroup;
  addNewAddress = false;
  existingAddresses: Address[];
  filteredAddresses: Address[];
  submitted = false;

  constructor(private formBuilder: FormBuilder, private addressService: AddressService) { }

  ngOnInit(): void {
    this.addAddressForm = this.formBuilder.group({
      addressName: ['', [Validators.required]],
      lineOne: ['', [Validators.required]],
      lineTwo: [''],
      city: ['', [Validators.required]],
      postcode: ['', [Validators.required]],
      country: ['', [Validators.required]]
    });

    this.selectAddressForm = this.formBuilder.group({
      selectedAddress: [''],
      nameFilter: ['']
    });

    this.addressService.findAllByCriteria(0, 1000).subscribe((response) => {
      this.existingAddresses = response;
      this.filteredAddresses = response;
    });
  }

  addAddress(): void {
    this.submitted = true;

    if (this.addAddressForm.valid) {
      const addedAddress = new Address(null,
        this.addAddressForm.value.addressName,
        this.addAddressForm.value.lineOne,
        this.addAddressForm.value.lineTwo,
        this.addAddressForm.value.city,
        this.addAddressForm.value.postcode,
        this.addAddressForm.value.country,
        null,
        true
      );

      this.addressService.addAddress(addedAddress).subscribe((response) => {
        this.existingAddresses.push(
          response
        );

        this.addNewAddress = false;
        this.selectAddressForm.value.selectedAddress = response;
        this.addressSelected();
      });
    }
  }

  addressSelected(): void {
    this.locationChanged.emit(this.selectAddressForm.value.selectedAddress);
  }

  /**
   * filter the array of addresses by name
   */
  filterAddresses(): void {
    const nameFilter = this.selectAddressForm.value.nameFilter;
    this.filteredAddresses = this.existingAddresses.filter(
      (value) => {
        if (nameFilter !== '') {
          if (!value.name.includes(nameFilter)) {
            return false;
          }
        }

        return true;
      });
  }
}
