import { Sector } from "./sector";

export interface SeatUnit {
  id: number;
  sector: Sector;
  available: boolean;
  customLabel: string;
}
