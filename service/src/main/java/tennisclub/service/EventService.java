package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface EventService {

    Event reschedule(Event event, LocalDateTime newStart, LocalDateTime newEnd);

    Event findById(Long id);

    List<Event> findAll();

    List<Event> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    List<Event> findByStartTime(LocalDateTime start);

    List<Event> findByEndTime(LocalDateTime end);

}
