import { SectorType } from './sectortype';

export class TicketType {
  constructor(
    public id: number,
    public title: string,
    public price: number,
    public sectorType: SectorType
  ) {}
}
