import {Ticket} from './ticket';
import {CustomFile} from './customFile';
import {User} from './user';

export class Booking {
  constructor(
    public id: number,
    public buyDate: Date,
    public user: User,
    public tickets: Ticket[],
    public invoice: CustomFile
  ) {}
}
