import {Performance} from './performance';
import {CustomFile} from './customFile';

export class Event {
  constructor(
    public id: number,
    public name: string,
    public description: string,
    public startDate: Date,
    public endDate: Date,
    public eventType: 'CINEMA' | 'THEATRE' | 'OPERA' | 'CONCERT',
    public duration: number,
    public performances: Performance[],
    public images: CustomFile[]
  ) {}
}
