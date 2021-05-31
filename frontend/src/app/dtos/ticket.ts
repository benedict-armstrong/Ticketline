import {TicketType} from './ticketType';
import {Performance} from './performance';
import {LayoutUnit} from './layoutUnit';

export class Ticket {
  constructor(
    public id: number,
    public performance: Performance,
    public seat: LayoutUnit,
    public ticketType: TicketType,
    public totalPrice: number,
    public status: 'PAID' | 'RESERVED' | 'CANCELLED'
  ) {}
}
