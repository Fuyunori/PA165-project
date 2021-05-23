import {Component, Input, OnInit} from '@angular/core';
import {Observable, of} from "rxjs";
import {EventType, Event} from "../../models/event.model";

enum EventTableColumn {
  Type = 'Type',
  StartTime = 'StartTime',
  EndTime = 'EndTime',
}

@Component({
  selector: 'tc-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit {
  @Input() displayedEvents$: Event[] = [];

  readonly EventTableColumn = EventTableColumn;
  readonly eventColumns: EventTableColumn[] = [
    EventTableColumn.Type,
    EventTableColumn.StartTime,
    EventTableColumn.EndTime,
  ];

  readonly typeNames: Record<EventType, string> = {
    LESSON: 'Lesson',
    BOOKING: 'Booking',
    TOURNAMENT: 'Tournament',
  };

  constructor() { }

  ngOnInit(): void {
  }

  asEventType(type: any): EventType {
    return type;
  }

}
