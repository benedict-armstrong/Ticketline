import { Pipe, PipeTransform } from '@angular/core';
import {TicketType} from '../../../dtos/ticketType';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(items: TicketType[], sector: number) {
    if (!items || !sector) {
      return items;
    }

    return items.filter((item) => item.sector.id === sector);
  }

}
