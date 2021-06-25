import { Component, OnInit } from '@angular/core';
import {BookingService} from '../../services/booking.service';
import {Booking} from '../../dtos/booking';
import {ChangeBooking} from '../../dtos/changeBooking';
import {FileService} from '../../services/file.service';

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

  onBuyClick(booking) {
    const changeBooking = new ChangeBooking(booking.id, 'PAID_FOR');
    this.bookingService.updateBooking(changeBooking).subscribe(
      (response) => {
        booking.status = response.status;
      }, error => {
        console.error(error);
      }
    );
  }

  onStornoClick(id) {
    alert('Storno: ' + id);
  }
}
