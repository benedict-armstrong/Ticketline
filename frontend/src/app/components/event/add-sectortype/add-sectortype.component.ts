import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SectorType} from '../../../dtos/sectortype';

@Component({
  selector: 'app-add-sectortype',
  templateUrl: './add-sectortype.component.html',
  styleUrls: ['./add-sectortype.component.scss']
})
export class AddSectortypeComponent implements OnInit {
  @Input() sectorTypes: SectorType[];
  @Output() sectorTypesChange = new EventEmitter<SectorType[]>();
  submitted = false;
  addSectorTypeForm: FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.addSectorTypeForm = this.formBuilder.group({
      sectorName: ['', Validators.required],
      sectorNumberOfTickets: ['', [Validators.required, Validators.min(1)]],
      sectorPrice: ['', [Validators.required, Validators.min(0)]]
    });
  }

  addSectorType() {
    this.submitted = true;

    if (this.addSectorTypeForm.valid) {
      const sectorType = new SectorType(null,
        this.addSectorTypeForm.value.sectorName,
        this.addSectorTypeForm.value.sectorNumberOfTickets,
        this.addSectorTypeForm.value.sectorPrice);

      this.sectorTypes.push(sectorType);
      this.sectorTypesChange.emit(this.sectorTypes);

      this.addSectorTypeForm.controls.sectorName.reset();
      this.addSectorTypeForm.controls.sectorNumberOfTickets.reset();
      this.submitted = false;
    }
  }

  removeSectorType(index) {
    if (index > -1) {
      this.sectorTypes.splice(index, 1);
    }

    this.sectorTypesChange.emit(this.sectorTypes);
  }
}
