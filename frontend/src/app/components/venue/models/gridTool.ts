import { Sector } from './sector';

export interface GridTool {
  action: 'add' | 'remove' | 'assignSector';
  scope: 'single' | 'row' | 'column';
  sector: Sector;
  reassign: boolean;
}
