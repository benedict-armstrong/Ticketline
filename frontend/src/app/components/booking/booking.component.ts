import { Component, OnInit } from '@angular/core';
import {BookingService} from '../../services/booking.service';
import {Booking} from '../../dtos/booking';
import {ChangeBooking} from '../../dtos/changeBooking';
import {ViewportScroller} from '@angular/common';

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.scss']
})
export class BookingComponent implements OnInit {
  loading = true;
  empty = true;
  cancelled = false;
  bought = false;
  showAll = false;

  bookings: Booking[] = [];
  allBookings: Booking[] = [];

  constructor(private bookingService: BookingService, private scroll: ViewportScroller) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.loadBookings();
  }

  loadBookings() {
    this.bookingService.getBookings().subscribe(
      (response) => {
        this.allBookings.push(...response);
        this.allBookings.reverse();

        this.bookings.push(
          ...response.filter(booking => booking.status !== 'CANCELLED')
        );
        this.bookings.reverse();
        if (this.bookings.length > 0 ) {
          this.empty = false;
        }
        this.loading = false;
      }, error => {
        console.error(error);
      }
    );
  }

  onBuyClick(booking) {
    const changeBooking = new ChangeBooking(booking.id, 'PAID_FOR');
    this.bookingService.updateBooking(changeBooking).subscribe(
      (response) => {
        booking.status = response.status;
        this.bought = true;
        this.scrollToTop();
      }, error => {
        console.error(error);
      }
    );
  }

  onStornoClick(booking) {
    const changeBooking = new ChangeBooking(booking.id, 'CANCELLED');
    this.bookingService.updateBooking(changeBooking).subscribe(
      (response) => {
        if (booking.status !== 'RESERVED') {
          this.cancelled = true;
        }

        booking.status = response.status;

        this.scrollToTop();
      }, error => {
        console.error(error);
      }
    );
  }

  scrollToTop() {
    this.scroll.scrollToPosition([0, 0]);
  }

  showCancelledOrders(): void {
    this.scrollToTop();
    this.showAll = true;

    if (this.allBookings.length > 0) {
      this.empty = false;
    }
  }
}
