package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.BookingDao;
import tennisclub.entity.Booking;
import tennisclub.entity.User;
import tennisclub.exceptions.TennisClubManagerException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    final private BookingDao bookingDao;

    @Autowired
    public BookingServiceImpl(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
    }

    @Override
    public Booking createBooking(Booking booking) {
        bookingDao.create(booking);
        return booking;
    }

    @Override
    public Booking updateBooking(Booking booking) {
        return bookingDao.update(booking);
    }

    @Override
    public void deleteBooking(Booking booking) {
        bookingDao.delete(booking);
    }

    @Override
    public void removeUser(Booking booking, User user) {
        if (!booking.getUsers().contains(user)) {
            throw new TennisClubManagerException("Removing user from a booking he is not in!");
        }
        user.removeBooking(booking);
    }

    @Override
    public void addUser(Booking booking, User user) {
        if (booking.getUsers().contains(user)) {
            throw new TennisClubManagerException("Adding user to a booking he is already in!");
        }
        user.addBooking(booking);
    }

    @Override
    public List<Booking> findAll() {
        return bookingDao.findAll();
    }

    @Override
    public Booking findById(Long id) {
        return bookingDao.findById(id);
    }

    @Override
    public List<Booking> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return bookingDao.findByTimeInterval(from, to);
    }
}
