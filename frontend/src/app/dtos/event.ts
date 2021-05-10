import {CustomFile} from './customFile';

export class Event {
  constructor(
    public id: number,
    public title: string,
    public description: string,
    public date: Date,
    public duration: number,
    public images: CustomFile[]
  ) {}
}
