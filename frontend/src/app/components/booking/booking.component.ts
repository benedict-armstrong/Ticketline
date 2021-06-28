import { Component, OnInit } from '@angular/core';
import {BookingService} from '../../services/booking.service';
import {Booking} from '../../dtos/booking';
import {ChangeBooking} from '../../dtos/changeBooking';
import {FileService} from '../../services/file.service';
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
        //console.error(error);
      }
    );
  }

  downloadClick(booking: Booking) {
    const pdf = booking.invoice;
    const newDate = new Date(booking.createDate);
    const filename = newDate.getDate() + '-' + newDate.getMonth() + '-' + newDate.getFullYear();

    const a = document.createElement('a');
    document.body.appendChild(a);
    const url = window.URL.createObjectURL(FileService.asFile(pdf.data, pdf.type));
    a.href = url;
    a.download = filename + '.pdf';
    a.click();
    window.URL.revokeObjectURL(url);
  }

  onBuyClick(booking: Booking) {
    const changeBooking = new ChangeBooking(booking.id, 'PAID_FOR');
    this.bookingService.updateBooking(changeBooking).subscribe(
      (response) => {
        const bookIndex = this.bookings.indexOf(booking);
        const allBookIndex = this.allBookings.indexOf(booking);
        booking = response;
        this.bookings[bookIndex] = booking;
        this.allBookings[allBookIndex] = booking;
        
        this.bought = true;
        this.scrollToTop();
      }, error => {
        //console.error(error);
      }
    );
  }

  onStornoClick(booking: Booking) {
    const changeBooking = new ChangeBooking(booking.id, 'CANCELLED');
    this.bookingService.updateBooking(changeBooking).subscribe(
      (response) => {
        if (response == null) { // From reserved to canceled
          this.bookings = this.bookings.filter(item => item.id !== booking.id);
          this.allBookings = this.allBookings.filter(item => item.id !== booking.id);
        } else {
          const bookIndex = this.bookings.indexOf(booking);
          const allBookIndex = this.allBookings.indexOf(booking);
          booking = response;
          this.bookings[bookIndex] = booking;
          this.allBookings[allBookIndex] = booking;
          this.cancelled = true;
        }


        this.scrollToTop();
      }, error => {
        //console.error(error);
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
