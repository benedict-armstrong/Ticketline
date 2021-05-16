export interface SeatUnit {
  sectorId: number;
  type: "seating" | "standing";
  available: boolean;
}
