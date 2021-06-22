import {CustomFile} from './customFile';
import { Ticket } from './ticket';

export class Booking {
  constructor(
    public id: number,
    public createDate: Date,
    public tickets: Ticket[],
    public invoice: CustomFile,
    public status: string
  ) {}
}
