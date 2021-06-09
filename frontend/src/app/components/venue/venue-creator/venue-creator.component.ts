import { EventEmitter, HostListener, Output } from '@angular/core';
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GridTool } from '../models/gridTool';
import { SeatUnit } from '../models/seatUnit';
import { Sector } from 'src/app/dtos/sector';
import { LayoutUnit } from 'src/app/dtos/layoutUnit';

@Component({
  selector: 'app-venue-creator',
  templateUrl: './venue-creator.component.html',
  styleUrls: ['./venue-creator.component.scss'],
})
export class VenueCreatorComponent implements OnInit {
  @Input()
  sectors: Sector[] = [];

  @Output() createdVenueLayout = new EventEmitter<LayoutUnit[][]>();

  venueLayoutForm: FormGroup;
  submitted = false;
  venueLayout: SeatUnit[][];
  frontText = 'Front';

  activeTool: GridTool = {
    action: 'remove',
    scope: 'single',
    reassign: true,
    sector: null,
  };

  constructor(private formBuilder: FormBuilder) {}

  @HostListener('window:beforeunload', ['$event'])
  doSomething($event) {
    if (this.venueLayout) {
      $event.returnValue = 'Your data will be lost!';
    }
  }

  ngOnInit(): void {
    this.venueLayoutForm = this.formBuilder.group({
      gridSizeX: [
        20,
        [Validators.required, Validators.min(10), Validators.max(500)],
      ],
      gridSizeY: [
        20,
        [Validators.required, Validators.min(10), Validators.max(500)],
      ],
    });

    if (this.sectors.length > 0) {
      this.activeTool.sector = this.sectors[0];
    }

    this.makeGrid();
  }

  makeGrid(): void {
    if (this.venueLayoutForm.valid) {
      if (this.submitted && !confirm('Update? All changes will be lost.')) {
        this.submitted = true;
        return;
      }
      this.submitted = true;
      this.activeTool.action = 'remove';
      this.venueLayout = new Array(this.venueLayoutForm.value.gridSizeX);

      for (let i = 0; i < this.venueLayoutForm.value.gridSizeX; i++) {
        this.venueLayout[i] = new Array(this.venueLayoutForm.value.gridSizeY);
        for (let j = 0; j < this.venueLayoutForm.value.gridSizeY; j++) {
          this.venueLayout[i][j] = {
            id: this.venueLayoutForm.value.gridSizeX * i + j,
            sector: null,
            available: true,
            customLabel: (
              this.venueLayoutForm.value.gridSizeX * i +
              j
            ).toString(),
          };
        }
      }
    }
  }

  toggleAdd() {
    this.activeTool.action = 'add';
  }

  toggleRemove() {
    this.activeTool.action = 'remove';
  }

  toggleSingle() {
    this.activeTool.scope = 'single';
  }

  toggleColumn() {
    this.activeTool.scope = 'column';
  }

  toggleRow() {
    this.activeTool.scope = 'row';
  }

  toggleSector() {
    this.activeTool.action = 'assignSector';
  }

  toggleReassign() {
    this.activeTool.reassign = !this.activeTool.reassign;
  }

  save() {
    let valid = true;
    const layout: LayoutUnit[][] = this.venueLayout.map((row) =>
      row.map((su) => {
        if (su.available && !su.sector) {
          valid = false;
        }
        if (su.available) {
          return new LayoutUnit(
            null,
            su.customLabel,
            su.sector ? su.sector.id : null
          );
        } else {
          return null;
        }
      })
    );

    if (valid) {
      this.createdVenueLayout.emit(layout);
    } else {
      alert('Please assign all seats');
    }
  }
}
