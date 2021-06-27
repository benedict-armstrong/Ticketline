import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {News} from '../../dtos/news';
import {FileService} from '../../services/file.service';

@Component({
  selector: 'app-news-item',
  templateUrl: './news-item.component.html',
  styleUrls: ['./news-item.component.scss']
})
export class NewsItemComponent implements OnInit, OnChanges {

  @Input() lastRead: number = null;
  @Input() removeBadge = false;

  item: News;
  imgURL: any;
  read = false;

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

  ngOnChanges(changes: SimpleChanges) {
    if (this.lastRead == null) {
      this.read = false;
    } else {
      this.read = this.lastRead >= this.item.id;
    }
    if (this.removeBadge) {
      this.read = true;
    }
  }

  /**
   * Sets the imgURL property (src of img) to the file's temporary URL.
   *
   * @param file the file to be displayed in the img tag
   * @private
   */
  private setURL(file: File) {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = _event => {
      this.imgURL = reader.result;
    };
  }

}
