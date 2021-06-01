import { Address } from './address';
import {News} from './news';

export class User {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public telephoneNumber: string,
    public email: string,
    public password: string,
    public lastLogin: Date,
    public lastReadNews: News,
    public points: number,
    public status: 'ACTIVE' | 'BANNED',
    public role: 'CLIENT' | 'ORGANIZER' | 'ADMIN',
    public address: Address) {
  }
}
