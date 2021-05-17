import {Component, Input, OnInit} from '@angular/core';
import {SectorType} from '../../dtos/sectortype';

@Component({
  selector: 'app-ticket-list-item',
  templateUrl: './ticket-list-item.component.html',
  styleUrls: ['./ticket-list-item.component.scss']
})
export class TicketListItemComponent implements OnInit {
  @Input()
  sectorType: SectorType;

  constructor() { }

  ngOnInit(): void {
  }

}
