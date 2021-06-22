import { TicketType } from './ticketType';
import { Performance } from './performance';

export class NewTicket {
  constructor(
    public performance: Performance,
    public ticketType: TicketType
  ) {}
}
