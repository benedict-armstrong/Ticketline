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
  noNews = false;

  private limit = 8; // Determines how many news entries are loaded at the beginning and with each click on Load More
  private offset = null;

  constructor(private newsService: ApplicationNewsService) { }

  ngOnInit(): void {
    this.loadBatch();
  }

  /**
   * Loads the next news entries to be displayed.
   * The amount of news entries in one batch is specified in the property `limit`.
   * Offsetting is done with the help of IDs.
   */
  loadBatch() {
    this.newsService.getNews(this.limit, this.offset).subscribe(
      response => {
        this.news.push(...response);
        if (response.length > 0) {
          this.offset = response[response.length - 1].id;
          this.noNews = false;
        } else {
          this.noNews = true;
        }
      }, error => {
        console.error(error);
      }
    );
  }

}
