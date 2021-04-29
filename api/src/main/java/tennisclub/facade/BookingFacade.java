package tennisclub.facade;

import tennisclub.dto.booking.BookingCreateDTO;
import tennisclub.dto.booking.BookingDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingFacade {

    BookingDTO create(BookingCreateDTO createDTO);

    void remove(Long bookingId);

    BookingDTO addUser(Long bookingId, Long UserId);

    BookingDTO removeUser(Long bookingId, Long UserId);

    List<BookingDTO> findAll();

    BookingDTO findById(Long bookingId);

    List<BookingDTO> findByTimeInterval(LocalDateTime from, LocalDateTime to);
}
