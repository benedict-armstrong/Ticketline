import {SectorType} from './sectortype';
import {Artist} from './artist';
import {Address} from './address';

export class Performance {
  constructor(
    public id: number,
    public title: string,
    public description: string,
    public date: string,
    public sectorTypes: SectorType[],
    public location: Address,
    public artist: Artist
  ) {}
}
