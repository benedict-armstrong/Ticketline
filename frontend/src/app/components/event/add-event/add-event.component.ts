import { Component, OnInit } from '@angular/core';
import {ApplicationEventService} from '../../../services/event.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {FileService} from '../../../services/file.service';
import {Event} from '../../../dtos/event';
import {Router} from '@angular/router';
import {Address} from '../../../dtos/address';
import {Artist} from '../../../dtos/artist';

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.scss']
})
export class AddEventComponent implements OnInit {
  addEventForm: FormGroup;
  submitted = false;
  fileNoImage = false;
  tooManyFiles = false;
  fileTooBig = false;
  files = [];
  sectorTypes = [];
  error = false;
  errorMessage: string;
  success = false;

  event = new Event(null, null, null, null, null, [], null, null, null, []);

  constructor(private applicationEventService: ApplicationEventService,
              private formBuilder: FormBuilder, private fileService: FileService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.addEventForm = this.formBuilder.group({
      eventName: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.maxLength(10000)]],
      files: [''],
      date: ['', [Validators.required, this.dateValidator]],
      eventType: ['CONCERT', [Validators.required]],
      artistFirstName: ['', [Validators.required]],
      artistLastName: ['', [Validators.required]],
      addressName: ['', [Validators.required]],
      lineOne: ['', [Validators.required]],
      lineTwo: [''],
      city: ['', [Validators.required]],
      postcode: ['', [Validators.required]],
      country: ['', [Validators.required]],
      duration: ['', [Validators.required, Validators.min(1)]]
    });
  }

  addEvent() {
    this.submitted = true;

    if (this.addEventForm.valid &&
      this.sectorTypes.length !== 0 &&
      this.files.length !== 0) {
      // Add additional event data
      this.event.title = this.addEventForm.value.eventName;
      this.event.description = this.addEventForm.value.description;
      this.event.date = this.addEventForm.value.date;
      this.event.duration = this.addEventForm.value.duration;
      this.event.eventType = this.addEventForm.value.eventType;
      this.event.location = new Address(
        null,
        this.addEventForm.value.addressName,
        this.addEventForm.value.lineOne,
        this.addEventForm.value.lineTwo,
        this.addEventForm.value.city,
        this.addEventForm.value.postcode,
        this.addEventForm.value.country
      );
      this.event.artist = new Artist(
        null,
        this.addEventForm.value.artistFirstName,
        this.addEventForm.value.artistLastName
      );
      this.event.sectorTypes = this.sectorTypes;

      // // New Imageupload
      let count = this.files.length;

      this.files.forEach(file => {
        this.fileService.upload(file).subscribe(f => {
          this.event.images.push(f);
          count--;
          if (count === 0) {
            this.applicationEventService.addEvent(this.event).subscribe(
              (response) => {
                this.success = true;
                this.router.navigate(['event-detail', response.id]);
              },
              error => {
                this.defaultServiceErrorHandling(error);
              }
            );
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
      } else if (this.event.images.length >= 10) {
        this.tooManyFiles = true;
      } else if (file.size > 1000000) {
        this.fileTooBig = true;
      } else {
        this.files.push(file);
        console.log(this.event.images);
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

  private dateValidator(control: FormControl): { invalidDate: boolean } {
    if (control.value) {
      const date = new Date(control.value);

      if (date.valueOf() < Date.now()) {
        return {invalidDate: true};
      }

      return null;
    }

    return null;
  }
}
