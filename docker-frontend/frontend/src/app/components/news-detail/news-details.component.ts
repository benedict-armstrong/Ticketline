import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { News } from 'src/app/dtos/news';
import { FileService } from 'src/app/services/file.service';
import { ApplicationNewsService } from 'src/app/services/news.service';
import {AuthService} from '../../services/auth.service';
import {UserService} from '../../services/user.service';
import {Event} from '../../dtos/event';
import {User} from '../../dtos/user';
import {ApplicationEventService} from '../../services/event.service';

@Component({
  selector: 'app-news-detail',
  templateUrl: './news-detail.component.html',
  styleUrls: ['./news-detail.component.scss']
})
export class NewsDetailComponent implements OnInit {

  error = false;
  errorMessage = '';
  success = false;
  newsId = 0;
  newsItem: News;
  correspondingEvent: Event;
  imgURL = [];
  date: Date;

  constructor(private newsService: ApplicationNewsService,
              private route: Router,
              private actRoute: ActivatedRoute,
              private authService: AuthService,
              private userService: UserService,
              private eventService: ApplicationEventService) {
  }

  ngOnInit(): void {
    this.newsId = this.actRoute.snapshot.params.id;
    this.newsService.getNewsById(this.newsId).subscribe(
      (response) => {
        this.newsItem = response;
        // this.correspondingEvent = this.newsItem.event;
        this.eventService.getEventById(this.newsItem.event).subscribe(
          (event) => {
            this.correspondingEvent = event;
          }, (error) => {
            this.defaultServiceErrorHandling(error);
          }
        );
        this.date = new Date(response.publishedAt);
        if (this.newsItem.images.length > 0) {
          for (let i = 0; i < this.newsItem.images.length; i++) {
            const img = FileService.asFile(this.newsItem.images[i].data, this.newsItem.images[i].type);
            this.setURL(img, i);
          }

          // console.log(this.imgURL);
        }
        this.markOlderAsRead();
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }

    );
  }

  vanishAlert(): void {
    this.error = false;
    this.success = false;
  }

  markOlderAsRead() {
    const email = this.authService.getUserEmail();
    let userToChange: User;

    if (email != null) {
      this.userService.getUserByEmail(email).subscribe(
        user => {
          userToChange = user;
          if (userToChange.lastReadNewsId == null || userToChange.lastReadNewsId < this.newsItem.id) {
            userToChange.lastReadNewsId = this.newsItem.id;
          }
          this.userService.updateLastRead(userToChange, userToChange.lastReadNewsId).subscribe(
            () => {
            }, error => {
              this.defaultServiceErrorHandling(error);
            }
          );
        }, error => {
          this.defaultServiceErrorHandling(error);
        }
      );
    }
  }

  private defaultServiceErrorHandling(error: any) {
    //console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }

  private setURL(file: File, id: number) {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = _event => {
      this.imgURL[id] = reader.result;
    };
  }

}
