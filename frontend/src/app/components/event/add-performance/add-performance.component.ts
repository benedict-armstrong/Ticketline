import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Performance} from '../../../dtos/performance';
import {Artist} from '../../../dtos/artist';
import {Event} from '../../../dtos/event';
import {Venue} from '../../../dtos/venue';
import {TicketType} from '../../../dtos/ticketType';

@Component({
  selector: 'app-add-performance',
  templateUrl: './add-performance.component.html',
  styleUrls: ['./add-performance.component.scss']
})
export class AddPerformanceComponent implements OnInit {
  @Output() performanceAdded = new EventEmitter<Performance>();
  @Output() cancelAdding = new EventEmitter();
  @Input() event: Event;
  addPerformanceForm: FormGroup;
  submitted = false;
  ticketTypes = [];

  error = false;
  errorMessage: string;
  success = false;

  performance: Performance = new Performance(null, null, null, null, null, null, null);
  artist: Artist;
  venue: Venue;

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.addPerformanceForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.maxLength(10000)]],
      date: ['', [Validators.required, this.dateValidator]],
    });
  }

  addPerformance() {
    this.submitted = true;

    console.log(this.ticketTypes);

    if (this.addPerformanceForm.valid && this.artist && this.venue) {
      this.performance = new Performance(
        null,
        this.addPerformanceForm.value.title,
        this.addPerformanceForm.value.description,
        new Date(this.addPerformanceForm.value.date).toISOString(),
        this.ticketTypes,
        this.artist,
        this.venue
      );

      this.performanceAdded.emit(this.performance);
      this.addPerformanceForm.reset();
    } else {
      console.log('Invalid Input');
    }
  }

  vanishAlert(): void {
    this.error = false;
    this.success = false;
  }

  changeArtist(artist: Artist) {
    this.artist = artist;
  }

  changeVenue(venue: Venue) {
    this.venue = venue;
  }

  changeTickets(ticketTypes: TicketType[]) {
    this.ticketTypes = ticketTypes;
  }

  /**
   * validate the date to be in the future.
   *
   * @param control where the date is picked
   * @private
   */
  private dateValidator(control: FormControl): { invalidDate: boolean } {
    if (control.value) {
      const startDate = new Date(control.value);
      if (startDate.valueOf() < Date.now()) {
        return {invalidDate: true};
      }

      return null;
    }

    return null;
  }
}
