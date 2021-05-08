import {Component, Input, OnInit} from '@angular/core';
import {News} from '../../dtos/news';

@Component({
  selector: 'app-news-item',
  templateUrl: './news-item.component.html',
  styleUrls: ['./news-item.component.scss']
})
export class NewsItemComponent implements OnInit {

  @Input() newsItem: News;

  constructor() { }

  ngOnInit(): void {
  }

}
