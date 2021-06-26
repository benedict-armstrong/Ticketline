import { Pipe, PipeTransform } from '@angular/core';
import {TicketType} from '../../../dtos/ticketType';
import {Sector} from '../../../dtos/sector';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(items: TicketType[], sector: Sector) {
    if (!items || !sector) {
      return items;
    }

    return items.filter((item) => item.sector.id === sector.id);
  }

}
