import {Component, EventEmitter, OnInit, Output} from '@angular/core';
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

  wasError = false;
  errorMessage: string;

  private page = 0;
  private size = 8; // Determines how many news entries are loaded at the beginning and with each click on Load More

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
    this.wasError = false;
    this.newsService.getNews(this.page, this.size).subscribe(
      response => {
        this.news.push(...response);
        if (response.length > 0) {
          this.page++;
        }
        if (response.length < this.size) {
          this.noNews = true;
        }
      }, error => {
        this.handleError(error);
      }
    );
  }

  private handleError(error: Error) {
    this.wasError = true;
    this.errorMessage = error.message;
  }

}
