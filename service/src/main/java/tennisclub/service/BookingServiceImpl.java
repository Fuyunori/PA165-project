package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.BookingDao;
import tennisclub.entity.Booking;
import tennisclub.entity.User;
import tennisclub.exceptions.ServiceLayerException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;
    private final TimeService timeService;

    @Autowired
    public BookingServiceImpl(BookingDao bookingDao, TimeService timeService) {
        this.bookingDao = bookingDao;
        this.timeService = timeService;
    }

    @Override
    public Booking create(Booking booking) {
        bookingDao.create(booking);
        return booking;
    }

    @Override
    public Booking update(Booking booking) {
        return bookingDao.update(booking);
    }

    @Override
    public void remove(Booking booking) {
        bookingDao.delete(booking);
    }

    @Override
    public Booking removeUser(Booking booking, User user) {
        if (!booking.getUsers().contains(user)) {
            throw new ServiceLayerException("Removing user from a booking he is not in!");
        }
        if (timeService.getCurrentDateTime().isAfter(booking.getStartTime())) {
            throw new ServiceLayerException("Can't remove user from booking after it started.");
        }
        user.removeBooking(booking);
        return bookingDao.update(booking);
    }

    @Override
    public Booking addUser(Booking booking, User user) {
        if (booking.getUsers().contains(user)) {
            throw new ServiceLayerException("Adding user to a booking he is already in!");
        }
        if (timeService.getCurrentDateTime().isAfter(booking.getStartTime())) {
            throw new ServiceLayerException("Can't add user to booking after it started.");
        }
        user.addBooking(booking);
        return bookingDao.update(booking);
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
