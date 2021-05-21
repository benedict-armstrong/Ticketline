import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ApplicationPerformanceService} from '../../../services/performance.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {FileService} from '../../../services/file.service';
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
  @Input() event: Event;
  addPerformanceForm: FormGroup;
  submitted = false;
  fileNoImage = false;
  tooManyFiles = false;
  fileTooBig = false;
  files = [];
  sectorTypes = [];
  error = false;
  errorMessage: string;
  success = false;

  performance = new Performance(null, null, null, null, null, null, null, []);
  location: Address;
  artist: Artist;

  constructor(private applicationEventService: ApplicationPerformanceService,
              private formBuilder: FormBuilder, private fileService: FileService) {
  }

  ngOnInit(): void {
    this.addPerformanceForm = this.formBuilder.group({
      eventName: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.maxLength(10000)]],
      files: [''],
      date: ['', [Validators.required, this.dateValidator]],
    });
  }

  addPerformance() {
    this.submitted = true;

    if (this.addPerformanceForm.valid &&
      this.sectorTypes.length !== 0 &&
      this.files.length !== 0 &&
      this.artist !== undefined &&
      this.location !== undefined) {
      // Add additional event data
      this.performance.title = this.addPerformanceForm.value.eventName;
      this.performance.description = this.addPerformanceForm.value.description;
      this.performance.date = this.addPerformanceForm.value.date;
      this.performance.location = this.location;
      this.performance.artist = this.artist;
      this.performance.sectorTypes = this.sectorTypes;

      // // New Imageupload
      let count = this.files.length;

      this.files.forEach(file => {
        this.fileService.upload(file).subscribe(f => {
          this.performance.images.push(f);
          count--;
          if (count === 0) {
            this.performanceAdded.emit(this.performance);
            this.addPerformanceForm.reset();
          }
        }, fileError => {
          this.defaultServiceErrorHandling(fileError);
        });
      });
    } else {
      console.log('Invalid input');
    }
  }

  onFileChange(event) {
    this.fileNoImage = false;
    this.tooManyFiles = false;
    this.fileTooBig = false;
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      console.log(file.size);
      if (!file.type.includes('image')) {
        this.fileNoImage = true;
      } else if (this.performance.images.length >= 10) {
        this.tooManyFiles = true;
      } else if (file.size > 1000000) {
        this.fileTooBig = true;
      } else {
        this.files.push(file);
        console.log(this.performance.images);
      }
    }
  }

  removeImage(index) {
    if (index > -1) {
      this.files.splice(index, 1);
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

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    console.log(error.error);
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
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
