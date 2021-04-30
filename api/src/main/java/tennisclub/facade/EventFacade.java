package tennisclub.facade;


import tennisclub.dto.event.EventDTO;
import tennisclub.dto.event.EventRescheduleDTO;
import tennisclub.dto.event.EventWithCourtDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface EventFacade {

    void reschedule(EventRescheduleDTO eventRescheduleDTO);

    EventWithCourtDTO findById(Long id);

    List<EventWithCourtDTO> findAll();

    List<EventWithCourtDTO> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    List<EventWithCourtDTO> findByStartTime(LocalDateTime start);

    List<EventWithCourtDTO> findByEndTime(LocalDateTime end);
}
