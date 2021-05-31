import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TicketType} from '../../../dtos/ticketType';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-add-tickettype',
  templateUrl: './add-ticket-type.component.html',
  styleUrls: ['./add-ticket-type.component.scss']
})
export class AddTicketTypeComponent implements OnInit {

  @Input() ticketTypes: TicketType[];

  submitted = false;
  addTicketTypeForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.addTicketTypeForm = this.formBuilder.group({
      name: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]],
      sector: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
  }

  addTicketType() {
    this.submitted = true;
    if (this.addTicketTypeForm.valid) {
      const ticketType = new TicketType(
        null,
        this.addTicketTypeForm.value.name,
        this.addTicketTypeForm.value.sector,
        this.addTicketTypeForm.value.price,
        );

      this.ticketTypes.push(ticketType);

      this.addTicketTypeForm.reset();
      this.submitted = false;
    }
  }

  removeTicketType(index: number) {
    if (index > -1) {
      this.ticketTypes.splice(index, 1);
    }
  }

}
