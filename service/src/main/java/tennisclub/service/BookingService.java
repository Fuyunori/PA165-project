package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Booking;
import tennisclub.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface BookingService {

    Booking createBooking(Booking booking);

    Booking updateBooking(Booking booking);

    void deleteBooking(Booking booking);

    Booking removeUser(Booking booking, User user);

    Booking addUser(Booking booking, User user);

    List<Booking> findAll();

    Booking findById(Long id);

    List<Booking> findByTimeInterval(LocalDateTime from, LocalDateTime to);
}
