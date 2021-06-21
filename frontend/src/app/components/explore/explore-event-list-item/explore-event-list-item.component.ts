import {Component, Input, OnInit} from '@angular/core';
import { TopEvent } from 'src/app/dtos/topEvent';
import {Event} from '../../../dtos/event';
import {FileService} from '../../../services/file.service';

@Component({
  selector: 'app-explore-event-list-item',
  templateUrl: './explore-event-list-item.component.html',
  styleUrls: ['./explore-event-list-item.component.scss']
})
export class ExploreEventListItemComponent implements OnInit {

  @Input() topEvent: TopEvent;
  @Input() data: {
    hot: boolean;
  };
  previewImageUrl: string | ArrayBuffer;

  constructor() { }

  ngOnInit(): void {
    const previewImage = this.topEvent.event.images[0];
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
