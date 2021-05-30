import { Component, OnInit, Input } from '@angular/core';
import {Performance} from '../../../dtos/performance';
import {Event} from '../../../dtos/event';
import { FileService } from 'src/app/services/file.service';

@Component({
  selector: 'app-performance-list-item',
  templateUrl: './performance-list-item.component.html',
  styleUrls: ['./performance-list-item.component.scss']
})
export class PerformanceListItemComponent implements OnInit {

  @Input() performance: Performance;
  @Input() event: Event;

  previewImageUrl: string | ArrayBuffer;

  constructor() { }

  ngOnInit(): void {
    if (this.event) {
      const previewImage = this.event.images[0];
      if (previewImage !== undefined) {
        const file = FileService.asFile(previewImage.data, previewImage.type);
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = _event => {
          this.previewImageUrl = reader.result;
        };
      }
    }
  }

}
