import { Component, Input, OnInit } from "@angular/core";
import { TicketType } from "src/app/dtos/ticketType";

@Component({
  selector: "app-ticket-list-item",
  templateUrl: "./ticket-list-item.component.html",
  styleUrls: ["./ticket-list-item.component.scss"],
})
export class TicketListItemComponent implements OnInit {
  @Input()
  ticketType: TicketType;

  constructor() {}

  ngOnInit(): void {}
}
