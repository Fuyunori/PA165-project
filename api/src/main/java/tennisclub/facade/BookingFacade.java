package tennisclub.facade;

import tennisclub.dto.booking.BookingCreateDTO;
import tennisclub.dto.booking.BookingFullDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingFacade {

    BookingFullDTO create(BookingCreateDTO createDTO);

    void remove(Long bookingId);

    BookingFullDTO addUser(Long bookingId, Long UserId);

    BookingFullDTO removeUser(Long bookingId, Long UserId);

    List<BookingFullDTO> findAll();

    BookingFullDTO findById(Long bookingId);

    List<BookingFullDTO> findByTimeInterval(LocalDateTime from, LocalDateTime to);
}
