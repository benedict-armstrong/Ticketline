import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ApplicationPerformanceService} from '../../../services/performance.service';
import {Performance} from '../../../dtos/performance';
import {Address} from '../../../dtos/address';
import {AddressService} from '../../../services/address.service';

@Component({
  selector: 'app-address-detail',
  templateUrl: './address-detail.component.html',
  styleUrls: ['./address-detail.component.scss']
})
export class AddressDetailComponent implements OnInit {

  address: Address;
  performances: Performance[] = [];
  page = 0;
  size = 8;
  noPerformance = false;

  constructor(private performanceService: ApplicationPerformanceService,
              private activeRoute: ActivatedRoute, private addressService: AddressService) { }

  ngOnInit(): void {
    const addressId = this.activeRoute.snapshot.params.id;

    this.addressService.getAddressById(addressId).subscribe(response => {
      this.address = response;
      this.getPerformancesForAddress();
    });
  }

  getPerformancesForAddress(): void {
    this.performanceService.getPerformancesForAddressId(this.address.id, this.page, this.size).subscribe(
      response => {
        this.performances.push(...response);
        if (response.length < this.size) {
          this.noPerformance = true;
        } else {
          this.page++;
          this.noPerformance = false;
        }
      }, error => {
        console.error(error);
      }
    );
  }

}
