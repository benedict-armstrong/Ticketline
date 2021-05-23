import { Sector } from "src/app/dtos/sector";

export interface GridTool {
  action: "add" | "remove" | "assignSector";
  scope: "single" | "row" | "column";
  sector: Sector;
  reassign: boolean;
}
