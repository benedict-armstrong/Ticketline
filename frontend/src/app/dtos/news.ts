import { Event } from './event';

export class News {
  constructor(
    public id: number,
    public publishedAt: string,
    public author: string,
    public title: string,
    public text: string,
    public event: Event,
    public picture: File[]
  ) {}
}
