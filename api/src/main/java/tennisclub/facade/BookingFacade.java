package tennisclub.facade;

import tennisclub.dto.booking.BookingCreateDTO;
import tennisclub.dto.booking.BookingFullDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miroslav Demek 
 */
public interface BookingFacade {

    Long create(BookingCreateDTO createDTO);

    void remove(Long bookingId);

    BookingFullDTO addUser(Long bookingId, Long userId);

    BookingFullDTO removeUser(Long bookingId, Long userId);

    List<BookingFullDTO> findAll();

    BookingFullDTO findById(Long bookingId);

    List<BookingFullDTO> findByTimeInterval(LocalDateTime from, LocalDateTime to);
}
