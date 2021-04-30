package tennisclub.facade;

import tennisclub.dto.event.EventRescheduleDTO;
import tennisclub.dto.event.EventWithCourtDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miroslav Demek
 */
public interface EventFacade {

    /**
     * Reschedule the given event to a new time.
     *
     * @param eventRescheduleDTO the information about which event to reschedule and the new time
     */
    void reschedule(EventRescheduleDTO eventRescheduleDTO);

    /**
     * Find the event with the specified id.
     *
     * @param id the id of the event.
     * @return the event with the id
     */
    EventWithCourtDTO findById(Long id);

    /**
     * Find all events.
     *
     * @return the list of all events
     */
    List<EventWithCourtDTO> findAll();

    /**
     * Find all events at least partially taking place during the specified time interval.
     *
     * @param from the start of the interval
     * @param to the end of the interval
     * @return all events in the interval
     */
    List<EventWithCourtDTO> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    /**
     * Find all events starting at the specified time.
     *
     * @param start the starting time
     * @return the events starting at the specified time
     */
    List<EventWithCourtDTO> findByStartTime(LocalDateTime start);

    /**
     * Find all events ending at the specified time
     *
     * @param end the ending time
     * @return the list of events with the ending time
     */
    List<EventWithCourtDTO> findByEndTime(LocalDateTime end);
}
