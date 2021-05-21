package tennisclub.service;

import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.entity.Event;

public interface EventMappingService {

    EventWithCourtDTO map(Event event);

}
