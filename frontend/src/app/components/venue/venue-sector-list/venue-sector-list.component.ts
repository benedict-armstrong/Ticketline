import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Sector } from '../models/sector';

@Component({
  selector: 'app-venue-sector-list',
  templateUrl: './venue-sector-list.component.html',
  styleUrls: ['./venue-sector-list.component.scss'],
})
export class VenueSectorListComponent implements OnInit {
  @Input()
  sector: Sector;

  @Output() deleteSector = new EventEmitter<Sector>();

  constructor() {}

  ngOnInit(): void {}

  delete() {
    this.deleteSector.emit(this.sector);
  }
}
