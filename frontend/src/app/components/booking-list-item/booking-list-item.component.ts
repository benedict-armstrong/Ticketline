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
  inThePast = false;

  constructor() { }

  ngOnInit(): void {
    for (const ticket of this.booking.tickets) {
      if (new Date(ticket.performance.date).getTime() < new Date().getTime()) {
        this.inThePast = true;
      }
    }
  }

  onStornoClick(booking) {
    this.cancelBooking.emit(booking);
  }

  onBuyClick(booking) {
    this.buyBooking.emit(booking);
  }

  onDownloadClick(booking) {
    this.downloadBooking.emit(booking);
  }

}
