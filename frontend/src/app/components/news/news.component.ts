import {Component, OnInit} from '@angular/core';
import {ApplicationNewsService} from '../../services/news.service';
import {News} from '../../dtos/news';
import {AuthService} from '../../services/auth.service';
import {User} from '../../dtos/user';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent implements OnInit {

  user: User;

  news: News[] = [];
  heldNews: News[] = [];
  noNews = false;
  loadingOnlyUnread = true;
  finishedAfter = false;
  lastReadNews: number = null; // Local copy used for comparison - should not be uploaded

  wasError = false;
  errorMessage: string;

  private page = 0;
  private size = 8; // Determines how many news entries are loaded at the beginning and with each click on Load More

  constructor(private newsService: ApplicationNewsService,
              private authService: AuthService,
              private userService: UserService) { }

  ngOnInit(): void {
    const email = this.authService.getUserEmail();

    if (email == null) {
      this.user = null;
      this.loadBatch();
    } else {
      this.userService.getUserByEmail(email).subscribe(
        user => {
          this.user = user;
          this.loadBatch();
        }, error => {
          console.log(error);
        }
      );
    }
  }

  /**
   * Loads the next news entries to be displayed.
   * The amount of news entries in one batch is specified in the property `limit`.
   * Offsetting is done with the help of IDs.
   */
  loadBatch() {
    this.wasError = false;

    // Fill up batch of unread with read news
    if (this.heldNews.length > 0) {
      this.news.push(...this.heldNews);
      this.heldNews = [];
      this.noNews = this.finishedAfter;
    } else {
      this.newsService.getNews(this.page, this.size).subscribe(
        response => {
          if (response.length > 0) {
            this.page++;
            let unread = [];
            let read = [];

            // Set unread and read according to LastReadNews
            if (this.user != null) {
              this.lastReadNews = this.user.lastReadNewsId;
              if (this.lastReadNews == null) {
                this.lastReadNews = 0;
              }
              unread = response.filter(item => item.id > this.lastReadNews);
              read = response.filter(item => item.id <= this.lastReadNews);
            } else {
              read = response;
              this.lastReadNews = response[0].id;
            }

            // push response and set variables for UI
            if (unread.length === this.size) {
              // Only unread
              this.news.push(...unread);
            } else if (unread.length < this.size && unread.length !== 0) {
              // Mixed
              this.loadingOnlyUnread = false;
              this.news.push(...unread);
              this.heldNews.push(...read);
              if (response.length < this.size) {
                this.finishedAfter = true;
              }
            } else if (read.length <= this.size) {
              // Read
              this.loadingOnlyUnread = false;
              this.news.push(...read);
              if (read.length < this.size) {
                this.noNews = true;
              }
            }
          } else {
           this.noNews = true;
          }

        }, error => {
          this.handleError(error);
        }
      );
    }
  }

  markAllAsRead() {
    this.loadingOnlyUnread = false;
    this.user.lastReadNewsId = this.news[0].id;
    this.userService.updateLastRead(this.user, this.user.lastReadNewsId).subscribe(
      user => {
        this.user = user;
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
