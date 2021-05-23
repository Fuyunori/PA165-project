package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Court;
import tennisclub.entity.Event;
import tennisclub.enums.CourtType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for court manipulation
 * @author Pavel Tobiáš
 */
@Service
public interface CourtService {
    /**
     * Adds a new court
     * @param court the court to add
     */
    void create(Court court);

    /**
     * Updates court information
     * @param court the court to update
     * @return the updated court
     */
    Court update(Court court);

    /**
     * Deletes a court
     * @param court the court to delete
     */
    void delete(Court court);

    /**
     * Retrieve a court with a given id
     * @param id the id of the court to be retrieved
     * @return the retrieved court
     */
    Court getById(Long id);

    /**
     * Retrieve all courts
     * @return a list of all courts
     */
    List<Court> listAll();

    /**
     * Lists all courts such that {@code addressSubstr} is a substring of their addresses
     * @param addressSubstr the address substring to match
     * @return a list of all matching courts
     */
    List<Court> listByAddress(String addressSubstr);

    /**
     * Lists all courts of a given type (grass, turf, ...)
     * @param type the type to match
     * @return a list of all matching courts
     */
    List<Court> listByType(CourtType type);

    /**
     * Assesses whether a court is free -- i.e. there are no events scheduled for it -- in a given date/time interval
     * @param court the court to check
     * @param from the left bound of the date/time interval
     * @param to the right bound of the date/time interval
     * @return a boolean indicating whether the court is free
     */
    boolean isFree(Court court, LocalDateTime from, LocalDateTime to);

    /**
     * Retrieve all events that conflict with the provided time slot on the given court
     *
     * @param court the court to check
     * @param from the left bound of the date/time interval
     * @param to the right bound of the date/time interval
     * @return the list of all conflicting events
     */
    public List<Event> getConflictingEvents(Court court, LocalDateTime from, LocalDateTime to);
}
