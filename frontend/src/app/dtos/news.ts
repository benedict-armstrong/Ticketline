import {CustomFile} from './customFile';

export class News {
  constructor(
    public id: number,
    public publishedAt: string,
    public author: string,
    public title: string,
    public text: string,
    public event: number,
    public images: CustomFile[]
  ) {}
}
