import {Component, Input, OnInit} from '@angular/core';
import {LayoutUnit} from '../../../dtos/layoutUnit';

@Component({
  selector: 'app-select-seat-unit',
  templateUrl: './select-seat-unit.component.html',
  styleUrls: ['./select-seat-unit.component.scss']
})
export class SelectSeatUnitComponent implements OnInit {

  @Input()
  layoutUnit: LayoutUnit;

  constructor() { }

  ngOnInit(): void {
  }

}
