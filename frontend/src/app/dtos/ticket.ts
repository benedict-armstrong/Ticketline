import {TicketType} from './ticketType';
import {Performance} from './performance';

export class Ticket {
  constructor(
    public id: number,
    public performance: Performance,
    public ticketType: TicketType,
    public seats: number[],
    public status: string
  ) {}
}
