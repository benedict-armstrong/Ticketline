import {Ticket} from './ticket';

export class CartItem {
  constructor(
    public id: number,
    public changeDate: string,
    public tickets: Ticket[],
    public status: string
  ) {}
}
