import {Component, Input, OnInit} from '@angular/core';
import {Performance} from '../../dtos/performance';
import {FileService} from '../../services/file.service';

@Component({
  selector: 'app-event-list-item',
  templateUrl: './event-list-item.component.html',
  styleUrls: ['./event-list-item.component.scss']
})
export class EventListItemComponent implements OnInit {
  @Input()
  eventItem: Performance;
  previewImageUrl: string | ArrayBuffer;

  constructor() { }

  ngOnInit(): void {
    const previewImage = this.eventItem.images[0];
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
