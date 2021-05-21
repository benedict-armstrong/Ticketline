import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Event} from '../../../dtos/event';
import {Performance} from '../../../dtos/performance';
import {EventService} from '../../../services/event.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.scss']
})
export class AddEventComponent implements OnInit {
  addEventForm: FormGroup;
  event = new Event(null, null, null, null, null, null, null, null);
  performances = [];
  submitted = false;
  addingPerformance = false;

  constructor(private formBuilder: FormBuilder, private eventService: EventService,
              private router: Router) { }

  ngOnInit(): void {
    this.addEventForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.maxLength(10000)]],
      duration: ['', [Validators.required, Validators.min(1)]],
      eventType: ['CONCERT', [Validators.required]],
      startDate: ['', [Validators.required, this.dateValidator]],
      endDate: ['', [Validators.required, this.dateValidator]],
    });
  }

  addEvent(): void {
    this.submitted = true;

    if (this.addEventForm.valid && this.performances.length !== 0) {
      this.event.name = this.addEventForm.value.name;
      this.event.description = this.addEventForm.value.description;
      this.event.duration = this.addEventForm.value.duration;
      this.event.eventType = this.addEventForm.value.eventType;
      this.event.startDate = this.addEventForm.value.startDate;
      this.event.endDate = this.addEventForm.value.endDate;
      this.event.performances = this.performances;

      this.eventService.addEvent(this.event).subscribe((response) => {
        this.router.navigate(['/']);
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
