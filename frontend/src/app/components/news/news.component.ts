import { Component, OnInit } from '@angular/core';
import {ApplicationNewsService} from '../../services/news.service';
import {News} from '../../dtos/news';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent implements OnInit {

  news: News[] = [];
  limit = 8; // Determines how many news entries are loaded at the beginning and with each click on Load More
  offset = 0;

  constructor(private newsService: ApplicationNewsService) { }

  ngOnInit(): void {
    this.loadBatch();
  }

  loadBatch() {
    this.newsService.getNews(this.limit, this.offset).subscribe(
      response => {
        this.news.push(...response);
        this.offset += this.limit;
      }, error => {
        console.error(error);
      }
    );
  }

}
