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
  noNews = true;

  wasError = false;
  errorMessage: string;

  loadingOnlyUnread = true;

  private page = 0;
  private size = 8; // Determines how many news entries are loaded at the beginning and with each click on Load More

  constructor(private newsService: ApplicationNewsService,
              private authService: AuthService,
              private userService: UserService) { }

  ngOnInit(): void {
    this.loadBatch();
    this.userService.getUserByEmail(
      this.authService.getUserEmail()
    ).subscribe(
      user => {
        this.user = user;
      }, error => {
        console.log(error);
      }
    );
  }

  /**
   * Loads the next news entries to be displayed.
   * The amount of news entries in one batch is specified in the property `limit`.
   * Offsetting is done with the help of IDs.
   */
  loadBatch() {
    if (!this.loadingOnlyUnread && this.heldNews.length > 0) {
      this.news.push(...this.heldNews);
      this.heldNews = [];
    }
    this.wasError = false;
    this.newsService.getNews(this.page, this.size).subscribe(
      response => {
        const unread = response.filter(item => item.id > this.user.lastReadNews.id);
        const read = response.filter(item => item.id <= this.user.lastReadNews.id);
        this.news.push(...unread);

        if (unread.length < this.size) {
          this.loadingOnlyUnread = false;
          this.heldNews.push(...read);
        } else {
          this.loadingOnlyUnread = true;
        }

        if (response.length > 0) {
          this.page++;
          this.noNews = false;
        }
        if (response.length < this.size) {
          this.noNews = true;
        }

      }, error => {
        this.handleError(error);
      }
    );
  }

  markAllAsRead() {
    this.user.lastReadNews = this.news[0];
    console.log('USR', this.user);
    this.userService.updateUser(this.user).subscribe(
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
