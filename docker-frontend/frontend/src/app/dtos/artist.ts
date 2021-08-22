import {Performance} from './performance';

export class Artist {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public performances: Performance[]
  ) {}
}
