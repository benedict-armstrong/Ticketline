import {User} from './user';
import {SectorType} from './sectortype';
import {TicketType} from './ticketType';
import {Performance} from './performance';

export class Ticket {
  constructor(
    public id: number,
    public owner: User,
    public performance: Performance,
    public sectorType: SectorType,
    public seats: number[],
    public ticketType: TicketType,
    public totalPrice: number,
    public status: string
  ) {}
}
