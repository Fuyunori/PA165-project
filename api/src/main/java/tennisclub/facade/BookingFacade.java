package tennisclub.facade;

import tennisclub.dto.booking.BookingCreateDTO;
import tennisclub.dto.booking.BookingFullDTO;
import tennisclub.dto.booking.BookingUpdateDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Facade for manipulating with bookings.
 *
 * @author Miroslav Demek
 */
public interface BookingFacade {

    /**
     * Create a new booking.
     *
     * @param createDTO the data for the new booking
     * @return full bookingDTO
     */
    BookingFullDTO makeBooking(BookingCreateDTO createDTO);

    /**
     * Cancel a booking.
     *
     * @param bookingId id of the booking to cancel
     */
    void cancelBooking(Long bookingId);

    /**
     * Add a new user to an existing booking.
     *
     * @param bookingId id of the booking
     * @param userId id of the user to add
     * @return the booking with added user
     */
    BookingFullDTO addUser(Long bookingId, Long userId);

    /**
     * Remove a user from an existing booking.
     *
     * @param bookingId id of the booking
     * @param userId id of the user to remove
     * @return the booking with removed user
     */
    BookingFullDTO removeUser(Long bookingId, Long userId);

    /**
     * Find all bookings.
     *
     * @return the list of all bookings
     */
    List<BookingFullDTO> findAll();

    /**
     * Find the booking with the specified id.
     *
     * @param bookingId the id of the booking
     * @return the booking with the id
     */
    BookingFullDTO findById(Long bookingId);

    /**
     * Find all bookings taking place at least partially during the specified interval.
     *
     * @param from the start of the interval
     * @param to the end of the interval
     * @return the booking in the interval
     */
    List<BookingFullDTO> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    /**
     * Update the specified booking.
     *
     * @param bookingId the id of the booking to update
     * @param dto the new values
     * @return the updated booking
     */
    BookingFullDTO update(Long bookingId, BookingUpdateDTO dto);
}
