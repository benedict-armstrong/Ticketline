import {Artist} from './artist';
import {TicketType} from './ticketType';
import {Venue} from './venue';

export class Performance {
  constructor(
    public id: number,
    public title: string,
    public description: string,
    public venue: Venue,
    public date: string,
    public ticketTypes: TicketType[],
    public artist: Artist
  ) {}
}
