import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Performance} from '../../../dtos/performance';
import {Artist} from '../../../dtos/artist';
import {Address} from '../../../dtos/address';
import {Event} from '../../../dtos/event';

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
  sectorTypes = [];
  error = false;
  errorMessage: string;
  success = false;

  performance = new Performance(null, null, null, null, null, null, null);
  location: Address;
  artist: Artist;

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

    this.performance.date = new Date(this.addPerformanceForm.value.date).toISOString();
    console.log(this.performance.date);
    if (this.addPerformanceForm.valid &&
      this.sectorTypes.length !== 0 &&
      this.artist !== undefined &&
      this.location !== undefined) {
      // Add additional event data
      this.performance.title = this.addPerformanceForm.value.title;
      this.performance.description = this.addPerformanceForm.value.description;
      this.performance.location = this.location;
      this.performance.artist = this.artist;
      this.performance.sectorTypes = this.sectorTypes;

      this.performance.date = new Date(this.addPerformanceForm.value.date).toISOString();
      console.log(this.performance.date);


      this.performanceAdded.emit(this.performance);
      this.addPerformanceForm.reset();
    } else {
      console.log('Invalid input');
    }
  }

  vanishAlert(): void {
    this.error = false;
    this.success = false;
  }

  changeLocation(location: Address) {
    this.location = location;
  }

  changeArtist(artist: Artist) {
    this.artist = artist;
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
