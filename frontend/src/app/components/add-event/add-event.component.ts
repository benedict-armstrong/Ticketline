import { Component, OnInit } from '@angular/core';
import {ApplicationEventService} from '../../services/event.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {FileService} from '../../services/file.service';
import {Event} from '../../dtos/event';
import {Router} from '@angular/router';
import {Address} from '../../dtos/address';
import {Artist} from '../../dtos/artist';
import {SectorType} from '../../dtos/sectortype';

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
  files = [];
  sectorTypes = [];
  sectorError = {
    name: false,
    amountTickets: false
  };
  error = false;
  errorMessage: string;
  success = false;

  event = new Event(null, null, null, null, null, [], null, null, null, []);

  constructor(private applicationEventService: ApplicationEventService,
              private formBuilder: FormBuilder, private fileService: FileService,
              private router: Router) {

    this.addEventForm = this.formBuilder.group({
      eventName: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.maxLength(10000)]],
      files: [''],
      date: ['', [Validators.required]],
      eventType: ['CONCERT', [Validators.required]],
      sectorName: [''],
      sectorAmountTickets: [''],
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

  ngOnInit(): void {
  }

  addEvent() {
    this.submitted = true;

    if (this.addEventForm.valid && this.sectorTypes.length !== 0) {
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

      // New Imageupload
      const fileService = this.fileService;
      let count = this.files.length;
      if (count === 0) {
        // Normal event without images
        this.applicationEventService.addEvent(this.event).subscribe(
          (response) => {
            this.success = true;
            this.router.navigate(['event-detail', response.id]);
          },
          error => {
            this.defaultServiceErrorHandling(error);
          }
        );
      } else {
        // With images
        this.files.forEach(file => {
          fileService.upload(file).subscribe(f => {
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
          });
        });
      }
    } else {
      console.log('Invalid input');
    }
  }

  addSectorType() {
    this.sectorError.name = false;
    this.sectorError.amountTickets = false;
    const sectorName = this.addEventForm.value.sectorName;
    const sectorAmountTickets = this.addEventForm.value.sectorAmountTickets;

    if (sectorName === '') {
      this.sectorError.name = true;
    }

    if (sectorAmountTickets === '' || sectorAmountTickets === 0) {
      this.sectorError.amountTickets = true;
    }

    if (this.sectorError.name || this.sectorError.amountTickets) {
      return;
    }

    const sectorType = new SectorType(null, sectorName, sectorAmountTickets);
    this.sectorTypes.push(sectorType);
  }

  onFileChange(event) {
    this.fileNoImage = false;
    this.tooManyFiles = false;
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      if (!file.type.includes('image')) {
        this.fileNoImage = true;
      } else if (this.event.images.length >= 10) {
        this.tooManyFiles = true;
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

  removeSectorType(index) {
    if (index > -1) {
      this.sectorTypes.splice(index, 1);
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

}
