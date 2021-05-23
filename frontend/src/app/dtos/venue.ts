import { Address } from "./address";
import { LayoutUnit } from "./layoutUnit";
import { Sector } from "./sector";

export class Venue {
  constructor(
    public id: number,
    public name: string,
    public address: Address,
    public sectors: Sector[],
    public layout: LayoutUnit[][]
  ) {}
}
