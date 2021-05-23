import { Pipe, PipeTransform } from '@angular/core';
import { Event } from '../models/event.model'

@Pipe({
  name: 'sortEvents'
})
export class SortEventsPipe implements PipeTransform {

  transform(events: Event[]): Event[] {
    events.sort((a: Event, b: Event) => {
      return new Date(a.startTime).getTime() - new Date(b.startTime).getTime();
    });
    return events;
  }

}
