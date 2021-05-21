import {Event} from './event';

export class Artist {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public events: Event[]
  ) {}
}
