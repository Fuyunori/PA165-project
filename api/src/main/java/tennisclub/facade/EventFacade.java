package tennisclub.facade;


import tennisclub.dto.event.EventDTO;
import tennisclub.dto.event.EventRescheduleDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface EventFacade {

    void reschedule(EventRescheduleDTO eventRescheduleDTO);

    EventDTO findById(Long id);

    List<EventDTO> findAll();

    List<EventDTO> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    List<EventDTO> findByStartTime(LocalDateTime start);

    List<EventDTO> findByEndTime(LocalDateTime end);
}
