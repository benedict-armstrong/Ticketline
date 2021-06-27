export class Sector {
  constructor(
    public id: number,
    public name: string,
    public type: 'STANDING' | 'SEATED' | 'STAGE',
    public color: string,
    public description?: string
  ) {}
}
