import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { News } from 'src/app/dtos/news';
import { ApplicationNewsService } from 'src/app/services/news.service';

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

  constructor(private newsService: ApplicationNewsService,  private route: Router, private actRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.newsId = this.actRoute.snapshot.params.id;
    this.newsService.getNewsById(this.newsId).subscribe(
      (response) => {
        this.newsItem = response;
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



  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }

}
