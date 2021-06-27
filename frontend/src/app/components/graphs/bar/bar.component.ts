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

  constructor() { }

  ngOnInit(): void {}
}
