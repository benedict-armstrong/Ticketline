import {Component, Input, OnInit} from '@angular/core';
import {News} from '../../dtos/news';
import {FileService} from '../../services/file.service';

@Component({
  selector: 'app-news-item',
  templateUrl: './news-item.component.html',
  styleUrls: ['./news-item.component.scss']
})
export class NewsItemComponent implements OnInit {

  item: News;
  imgURL: any;

  @Input() set newsItem(item: News) {
    this.item = item;
    if (item.images.length > 0) {
      const img = FileService.asFile(item.images[0].data, item.images[0].type);
      this.setURL(img);
    }
  }

  constructor() { }

  ngOnInit(): void {
  }

  private setURL(file: File) {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = _event => {
      this.imgURL = reader.result;
    };
  }

}
