import {Component, Input, OnInit} from '@angular/core';
import {GraphData} from '../graphData';

@Component({
  selector: 'app-bar',
  templateUrl: './bar.component.html',
  styleUrls: ['./bar.component.scss']
})
export class BarComponent implements OnInit {
  @Input()
  data: GraphData;

  heights: number[];

  constructor() { }

  ngOnInit(): void {
    this.calculateHeights();
  }

  calculateHeights() {
    const max = this.data.values.reduce((a, b) => Math.max(a, b));
    this.heights = this.data.values.map((a: number) => a / max);
  }

}
