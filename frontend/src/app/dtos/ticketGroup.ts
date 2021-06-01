import {Ticket} from './ticket';
import {Event} from './event';
import {Performance} from './performance';

export class TicketGroup {
  constructor(
    public id: number,
    public event: Event,
    public tickets: Ticket[],
    public performance: Performance
  ) {}
}
