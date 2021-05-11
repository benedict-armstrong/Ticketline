import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { News } from 'src/app/dtos/news';
import { FileService } from 'src/app/services/file.service';
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
  imgURL= [];
  date: Date;

  constructor(private newsService: ApplicationNewsService,  private route: Router, private actRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.newsId = this.actRoute.snapshot.params.id;
    this.newsService.getNewsById(this.newsId).subscribe(
      (response) => {
        this.newsItem = response;
        this.date = new Date(response.publishedAt);
        if (this.newsItem.images.length > 0) {
          //const img = FileService.asFile(this.newsItem.images[0].data, this.newsItem.images[0].type);

          for(let i=0; i<this.newsItem.images.length; i++){
            const img = FileService.asFile(this.newsItem.images[i].data, this.newsItem.images[i].type);
            this.setURL(img, i);
          }

          console.log(this.imgURL);
        }
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

  private setURL(file: File, id: number) {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = _event => {
      this.imgURL[id] = reader.result;
    };
  }

}
