import { Event } from './event';
import { CustomImage } from './customImage';

export class News {
  constructor(
    public id: number,
    public publishedAt: string,
    public author: string,
    public title: string,
    public text: string,
    public event: Event,
    public customImages: CustomImage[]
  ) {}
}
