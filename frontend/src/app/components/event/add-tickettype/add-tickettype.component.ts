import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TicketType} from '../../../dtos/ticketType';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-add-tickettype',
  templateUrl: './add-tickettype.component.html',
  styleUrls: ['./add-tickettype.component.scss']
})
export class AddTickettypeComponent implements OnInit {

  @Input() ticketTypes: TicketType[];

  submitted = false;
  addTicketTypeForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.addTicketTypeForm = this.formBuilder.group({
      ticketTypeTitle: ['', Validators.required],
      ticketTypeMultiplier: ['', [Validators.required, Validators.min(0)]],
    });
  }

  ngOnInit(): void {
  }

  addTicketType() {
    this.submitted = true;
    if (this.addTicketTypeForm.valid) {
      const ticketType = new TicketType(null,
        this.addTicketTypeForm.value.ticketTypeTitle,
        this.addTicketTypeForm.value.ticketTypeMultiplier, null);

      this.ticketTypes.push(ticketType);

      this.addTicketTypeForm.controls.ticketTypeTitle.reset();
      this.addTicketTypeForm.controls.ticketTypeMultiplier.reset();
      this.submitted = false;
    }
  }

  removeTicketType(index: number) {
    if (index > -1) {
      this.ticketTypes.splice(index, 1);
    }
  }

}
