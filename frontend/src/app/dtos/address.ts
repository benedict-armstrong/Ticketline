import {Performance} from './performance';

export class Address {
    constructor(
      public id: number,
      public name: string,
      public lineOne: string,
      public lineTwo: string,
      public city: string,
      public postcode: string,
      public country: string,
      public eventLocation: boolean) {
    }
  }
