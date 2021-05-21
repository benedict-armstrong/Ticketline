import {CustomFile} from './customFile';
import {SectorType} from './sectortype';
import {Artist} from './artist';
import {Address} from './address';

export class Event {
  constructor(
    public id: number,
    public title: string,
    public description: string,
    public date: Date,
    public eventType: 'CINEMA' | 'THEATRE' | 'OPERA' | 'CONCERT',
    public sectorTypes: SectorType[],
    public location: Address,
    public artist: Artist,
    public duration: number,
    public images: CustomFile[]
  ) {}
}
