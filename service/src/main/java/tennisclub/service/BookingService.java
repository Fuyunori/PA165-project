package tennisclub.service;

import tennisclub.entity.Booking;
import tennisclub.entity.Court;
import tennisclub.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    void createBooking(Booking booking);

    Booking updateBooking(Booking booking);

    void deleteBooking(Booking booking);

    void removeUser(Booking booking, User user);

    void addUser(Booking booking, User user);

    List<Booking> findALL();

    Booking findById(Long id);

    List<Booking> findByTimeInterval(LocalDateTime from, LocalDateTime to);
}
