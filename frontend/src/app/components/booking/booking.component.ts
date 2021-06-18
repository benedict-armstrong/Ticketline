import { Component, OnInit } from '@angular/core';
import {BookingService} from '../../services/booking.service';
import {Booking} from '../../dtos/booking';

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.scss']
})
export class BookingComponent implements OnInit {
  loading = true;
  empty = true;

  bookings: Booking[] = [];

  constructor(private bookingService: BookingService) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.loadBookings();
  }

  loadBookings() {
    this.bookingService.getBookings().subscribe(
      (response) => {
        this.bookings.push(...response);
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
    booking.status = 'PAID_FOR';
    this.bookingService.updateBooking(booking).subscribe(
      (response) => {
      }, error => {
        console.error(error);
      }
    );
  }

  onStornoClick(id) {
    alert('Storno: ' + id);
  }
}
