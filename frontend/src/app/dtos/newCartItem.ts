import { TicketType } from './ticketType';
import { Performance } from './performance';

export class NewCartItem {
  constructor(
    public performance: Performance,
    public ticketType: TicketType
  ) {}
}
