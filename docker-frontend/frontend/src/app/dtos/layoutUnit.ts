import {Sector} from './sector';

export class LayoutUnit {
  constructor(
    public id: number,
    public customLabel: string,
    public free: boolean,
    public sector: number | Sector
  ) {}
}
