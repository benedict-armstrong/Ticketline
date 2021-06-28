import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TicketType} from '../../../dtos/ticketType';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Sector} from '../../../dtos/sector';

@Component({
  selector: 'app-add-tickettype',
  templateUrl: './add-ticket-type.component.html',
  styleUrls: ['./add-ticket-type.component.scss']
})
export class AddTicketTypeComponent implements OnInit {

  @Input() sectors: Sector[];
  @Output() ticketTypesChanged = new EventEmitter<TicketType[]>();

  filteredSectors: Sector[];
  ticketTypes: TicketType[] = [];
  submitted = false;
  addTicketTypeForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {

    this.addTicketTypeForm = this.formBuilder.group({
      name: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]],
      sector: ['', [Validators.required]],
    });

    if (this.sectors) {
      this.filteredSectors = this.sectors.filter(sector => sector.type !== 'STAGE');
    }

  }

  addTicketType() {
    this.submitted = true;
    //console.log('Adding');
    if (this.addTicketTypeForm.valid) {
      const ticketType = new TicketType(
        null,
        this.addTicketTypeForm.value.name,
        this.addTicketTypeForm.value.sector,
        this.addTicketTypeForm.value.price * 100,
        );

      this.ticketTypes.push(ticketType);

      this.ticketTypesChanged.emit(this.ticketTypes);

      this.addTicketTypeForm.reset();
      this.submitted = false;
    }
  }

  remove(ticketType: TicketType) {
    this.ticketTypes = this.ticketTypes.filter(item => item.title !== ticketType.title ||
                                                       item.sector.id !== ticketType.sector.id ||
                                                       item.price !== ticketType.price);
    this.ticketTypesChanged.emit(this.ticketTypes);
  }

}
