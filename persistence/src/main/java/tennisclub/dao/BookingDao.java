package tennisclub.dao;

import tennisclub.entity.Booking;
import tennisclub.entity.Court;
import tennisclub.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO interface for basic CRUD operations on Booking.
 *
 * @author Miroslav Demek
 */
public interface BookingDao {

    /**
     * Insert the booking into the database.
     *
     * @param booking the booking to insert
     */
    void create(Booking booking);

    /**
     * Retrieve the booking with the specified id from the database.
     *
     * @param id the id to search for
     * @return the booking with the supplied id
     */
    Booking findById(Long id);

    /**
     * Retrieve all bookings on the specified court
     *
     * @param court the court whose bookings to retrieve
     * @return List of the bookings on the supplied court
     */
    List<Booking> findByCourt(Court court);

    /**
     * Retrieve all Bookings starting at the specified time.
     *
     * @param startTime the time when the bookings should start
     * @return List of bookings starting at the specified time
     */
    List<Booking> findByStartTime(LocalDateTime startTime);

    /**
     * Retrieve all Bookings ending at the specified time.
     *
     * @param endTime the time when the bookings should end
     * @return List of Bookings ending at the specified time
     */
    List<Booking> findByEndTime(LocalDateTime endTime);

    /**
     * Retrieve all bookings that at least partially take place
     * during the specified time interval. The interval is exclusive.
     *
     * More formally, retrieve all Events e such that:
     *     e.startTime < to && e.endTime > from
     *
     * The behaviour of this method is undefined if:
     *     from > to
     *
     * @param from the beginning of the interval
     * @param to the end of the interval
     * @return List of bookings in the interval
     */
    List<Booking> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    /**
     * Retrieve all bookings if the specified user
     *
     * @param user the user whose bookings to retrieve
     * @return List of the bookings of the user
     */
    List<Booking> findByUser(User user);

    /**
     * Retrieve all bookings.
     *
     * @return List of all booking.
     */
    List<Booking> findAll();

    /**
     * Persist the updated booking into the database.
     *
     * @param booking a booking with updated data to persist
     * @return the updated booking
     */
    Booking update(Booking booking);

    /**
     * Delete the specified booking from the database.
     *
     * @param booking the booking to delete
     */
    void delete(Booking booking);
}
