import {Sector} from './sector';

export class TicketType {
  constructor(
    public id: number,
    public title: string,
    public sector: Sector,
    public price: number // in Cent
  ) {}
}
