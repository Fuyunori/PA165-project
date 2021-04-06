package tennisclub.dao;

import tennisclub.entity.Court;
import tennisclub.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miroslav Demek
 */
public interface EventDao {

    /**
     * Insert the event into the database.
     *
     * @param event the event to insert
     */
    void create(Event event);

    /**
     * Retrieve the Event with the provided id.
     *
     * @param id the id of the event
     * @return the event with the id
     */
    Event findById(Long id);

    /**
     * Retrieve all events on the specified court.
     *
     * @param court the court whose events to retrieve
     * @return List of all events on the court
     */
    List<Event> findByCourt(Court court);

    /**
     * Retrieve all events starting at the specified time.
     *
     * @param startTime the time when the events should start
     * @return List of events starting at the specified time
     */
    List<Event> findByStartTime(LocalDateTime startTime);

    /**
     * Retrieve all events ending at the specified time.
     *
     * @param endTime the time when the events should end
     * @return List of events ending at the specified time
     */
    List<Event> findByEndTime(LocalDateTime endTime);

    /**
     * Retrieve all events that at least partially take place
     * during the specified time interval. The interval is inclusive.
     *
     * More formally, retrieve all Events e such that:
     *     e.startTime <= to && e.endTime >= from
     *
     * @param from the beginning of the interval
     * @param to the end of the interval
     * @return List of events in the interval
     */
    List<Event> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    /**
     * Retrieve all events.
     *
     * @return List of all events.
     */
    List<Event> findAll();

    /**
     * Persist the updated event into the database.
     *
     * @param event an event with updated data to persist
     * @return the updated event
     */
    Event update(Event event);

    /**
     * Delete the specified event from the database.
     *
     * @param event the event to delete
     */
    void delete(Event event);

}
