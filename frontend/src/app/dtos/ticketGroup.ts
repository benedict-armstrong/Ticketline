import {Ticket} from './ticket';

export class TicketGroup {
  constructor(
    public id: number,
    public tickets: Ticket[],
    public old: boolean,
    public reserved: boolean,
    public cancelled: boolean
  ) {}
}
