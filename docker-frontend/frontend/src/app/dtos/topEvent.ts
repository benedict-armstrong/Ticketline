import {Event} from './event';

export class TopEvent {
  constructor(
    public event: Event,
    public totalTickets: number,
    public soldTickets: number
  ) {}
}
