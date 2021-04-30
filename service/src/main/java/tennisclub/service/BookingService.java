package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Booking;
import tennisclub.entity.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface BookingService {

    Booking create(Booking booking);

    Booking update(Booking booking);

    void remove(Booking booking);

    Booking removeUser(Booking booking, User user);

    Booking addUser(Booking booking, User user);

    List<Booking> findAll();

    Booking findById(Long id);

    List<Booking> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    Duration getTotalReservedHoursToday(User user);
}
