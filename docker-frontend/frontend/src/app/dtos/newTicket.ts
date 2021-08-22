import { TicketType } from './ticketType';
import { Performance } from './performance';

export class NewTicket {
  constructor(
    public performanceId: number,
    public ticketType: TicketType,
    public amount: number,
    public seatId: number
  ) {}
}
