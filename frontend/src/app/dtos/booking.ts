import {CustomFile} from './customFile';
import {CartItem} from './cartItem';

export class Booking {
  constructor(
    public id: number,
    public buyDate: Date,
    public cartItems: CartItem[],
    public invoice: CustomFile
  ) {}
}
