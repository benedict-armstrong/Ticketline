import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Booking} from '../../dtos/booking';

@Component({
  selector: 'app-booking-list-item',
  templateUrl: './booking-list-item.component.html',
  styleUrls: ['./booking-list-item.component.scss']
})
export class BookingListItemComponent implements OnInit {
  @Input() booking: Booking;
  @Output() cancelBooking = new EventEmitter<Booking>();
  @Output() buyBooking = new EventEmitter<Booking>();
  @Output() downloadBooking = new EventEmitter<Booking>();

  constructor() { }

  ngOnInit(): void {
  }

  onStornoClick(booking) {
    booking.status = 'CANCELLED';
    this.cancelBooking.emit(booking);
  }

  onBuyClick(booking) {
    this.buyBooking.emit(booking);
  }

  onDownloadClick(booking) {
    this.downloadBooking.emit(booking);
  }

}
