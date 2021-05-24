import {Component, HostListener, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Event} from '../../../dtos/event';
import {Performance} from '../../../dtos/performance';
import {ApplicationEventService} from '../../../services/event.service';
import {Router} from '@angular/router';
import {ViewportScroller} from '@angular/common';
import {FileService} from '../../../services/file.service';

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.scss']
})
export class AddEventComponent implements OnInit {
  pageYoffset = 0;
  addEventForm: FormGroup;
  event = new Event(null, null, null, null, null, null, null, null, []);
  performances = [];
  files = [];
  fileNoImage = false;
  tooManyFiles = false;
  fileTooBig = false;
  submitted = false;
  addingPerformance = false;
  success = false;
  error = false;
  errorMessage: string;

  constructor(private formBuilder: FormBuilder, private eventService: ApplicationEventService,
              private router: Router, private scroll: ViewportScroller, private fileService: FileService) { }

  @HostListener('window:scroll', ['$event']) onScroll(event) {
    this.pageYoffset = window.pageYOffset;
  }

  ngOnInit(): void {
    this.addEventForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.maxLength(10000)]],
      duration: ['', [Validators.required, Validators.min(1)]],
      eventType: ['CONCERT', [Validators.required]],
      startDate: ['', [Validators.required, this.dateValidator]],
      endDate: ['', [Validators.required, this.dateValidator]],
      files: ['']
    });
  }

  addEvent(): void {
    this.submitted = true;

    if (this.addEventForm.valid
      && this.performances.length !== 0
      && this.files.length !== 0
    ) {
      this.event.name = this.addEventForm.value.name;
      this.event.description = this.addEventForm.value.description;
      this.event.duration = this.addEventForm.value.duration;
      this.event.eventType = this.addEventForm.value.eventType;
      this.event.startDate = this.addEventForm.value.startDate;
      this.event.endDate = this.addEventForm.value.endDate;
      this.event.performances = this.performances;

      // // New Imageupload
      let count = this.files.length;

      this.files.forEach(file => {
        this.fileService.upload(file).subscribe(f => {
          this.event.images.push(f);
          count--;
          if (count === 0) {
            this.eventService.addEvent(this.event).subscribe((response) => {
              this.success = true;
              this.router.navigate(['/event-detail', response.id]);
            }, error => {
              this.defaultServiceErrorHandling(error);
            });
          }
        }, fileError => {
          this.defaultServiceErrorHandling(fileError);
        });
      });
    }

  }

  addPerformance(performance: Performance): void {
    this.performances.push(performance);
    this.addingPerformance = false;
  }

  removePerformance(index: number): void {
    if (index > -1) {
      this.performances.splice(index, 1);
    }
  }

  changeToAddPerformance(): void {
    this.addingPerformance = true;
    this.event.name = this.addEventForm.value.name;
    this.scrollToTop();
  }

  scrollToTop() {
    this.scroll.scrollToPosition([0, 0]);
  }

  cancelAddingPerformance(): void  {
    this.addingPerformance = false;
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
