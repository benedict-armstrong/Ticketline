import { Component, OnInit, Input } from '@angular/core';
import {Performance} from '../../../dtos/performance';

@Component({
  selector: 'app-performance-list-item',
  templateUrl: './performance-list-item.component.html',
  styleUrls: ['./performance-list-item.component.scss']
})
export class PerformanceListItemComponent implements OnInit {

  @Input() performance: Performance;

  constructor() { }

  ngOnInit(): void {
  }

}
