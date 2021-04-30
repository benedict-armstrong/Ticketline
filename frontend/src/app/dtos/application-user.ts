import { Address } from './address';

export enum UserRole {
  CLIENT = 1,
  ORGANIZER = 2,
  ADMIN = 3
}

export enum Status {
  ACTIVE = 1,
  BANNED = 2
}

export class ApplicationUser {
  constructor(
    public id: number,
    public email: string,
    public password: string,
    public firstName: string,
    public lastName: string,
    public points: number,
    public address: Address,
    public userRole: UserRole,
    public status: Status,
    public lastVisited: number) {
  }
}