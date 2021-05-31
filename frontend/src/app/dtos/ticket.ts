import {TicketType} from './ticketType';
import {Performance} from './performance';
import {LayoutUnit} from './layoutUnit';

export class Ticket {
  constructor(
    public id: number,
    public ticketType: TicketType,
    public performance: Performance,
    public seats: number[],
    // public seat: LayoutUnit // delete seats uncomment this
  ) {}
}
