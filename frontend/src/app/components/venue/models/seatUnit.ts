import { Sector } from "src/app/dtos/sector";

export interface SeatUnit {
  id: number;
  sector: Sector;
  available: boolean;
  customLabel: string;
}
