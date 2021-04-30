import { Address } from './address';

export class User {
    constructor(
      public id: number,
      public email: string,
      public firstName: string,
      public lastName: string,
      public points: number,
      public Address: Address) {
    }
  }
  