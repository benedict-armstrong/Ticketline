import {Ticket} from './ticket';

export class CartItem {
  constructor(
    public id: number,
    public tickets: Ticket[]
  ) {}
}
