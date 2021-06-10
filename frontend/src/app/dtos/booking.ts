import {CustomFile} from './customFile';
import { Ticket } from './ticket';

export class Booking {
  constructor(
    public id: number,
    public buyDate: Date,
    public tickets: Ticket[],
    public invoice: CustomFile
  ) {}
}
