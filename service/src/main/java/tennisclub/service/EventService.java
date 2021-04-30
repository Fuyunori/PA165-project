package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miroslav Demek
 */
@Service
public interface EventService {

    /**
     * Reschedule an event to another time.
     *
     * @param event the event to reschedule
     * @param newStart the new starting time
     * @param newEnd the new ending time
     * @return the rescheduled event
     */
    Event reschedule(Event event, LocalDateTime newStart, LocalDateTime newEnd);

    /**
     * Find an event with the specified id.
     *
     * @param id the id of the event
     * @return the event with the id
     */
    Event findById(Long id);

    /**
     * Find all events.
     *
     * @return list of the events
     */
    List<Event> findAll();

    /**
     * Find all events that at least partially take place during the specified interval.
     *
     * @param from the start of the interval
     * @param to the end of the interval
     * @return the list of events in the interval
     */
    List<Event> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    /**
     * Find all events starting at the specified time.
     *
     * @param start the starting time
     * @return the list of events with the starting time
     */
    List<Event> findByStartTime(LocalDateTime start);

    /**
     * Find all events ending at the specified time.
     *
     * @param end the ending time
     * @return the list of events with the ending time
     */
    List<Event> findByEndTime(LocalDateTime end);
}
