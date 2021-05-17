export interface GridTool {
  action: "add" | "remove" | "assignSector";
  scope: "single" | "row" | "column";
  sectorId: number;
}
