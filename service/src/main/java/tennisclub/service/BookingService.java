package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Booking;
import tennisclub.entity.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miroslav Demek
 */
@Service
public interface BookingService {

    /**
     * Create a new booking.
     *
     * @param booking the booking to create
     * @return the created booking
     */
    Booking create(Booking booking);

    /**
     * Update the given booking.
     *
     * @param booking the booking to update
     * @return the updated booking
     */
    Booking update(Booking booking);

    /**
     * Remove the given booking.
     *
     * @param booking the booking to remove
     */
    void remove(Booking booking);

    /**
     * Remove the given user from the given booking.
     *
     * @param booking the booking whose participant to remove
     * @param user the user to remove
     * @return the updated booking
     */
    Booking removeUser(Booking booking, User user);

    /**
     * Add the given user from the given booking.
     *
     * @param booking the booking
     * @param user the user to remove
     * @return the updated booking
     */
    Booking addUser(Booking booking, User user);

    /**
     * Find all bookings.
     *
     * @return the list of all bookings
     */
    List<Booking> findAll();

    /**
     * Find the booking the specified id.
     *
     * @param id the id of the booking to find
     * @return the booking with the id
     */
    Booking findById(Long id);

    /**
     * Find all bookings that at least partially take place during the specified time interval.
     *
     * @param from the start of the interval
     * @param to the end of the interval
     * @return the list of all booking in the interval
     */
    List<Booking> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    /**
     * Get the total number of hours the specified user has reserved today.
     *
     * @param user the user whose reservations to count
     * @return the number of reserved hours
     */
    Duration getTotalReservedHoursToday(User user);
}
