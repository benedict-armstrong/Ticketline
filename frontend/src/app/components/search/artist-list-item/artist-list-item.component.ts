import {Component, Input, OnInit} from '@angular/core';
import {Artist} from '../../../dtos/artist';

@Component({
  selector: 'app-artist-list-item',
  templateUrl: './artist-list-item.component.html',
  styleUrls: ['./artist-list-item.component.scss']
})
export class ArtistListItemComponent implements OnInit {
  @Input() artist: Artist;

  constructor() { }

  ngOnInit(): void {
  }

}
