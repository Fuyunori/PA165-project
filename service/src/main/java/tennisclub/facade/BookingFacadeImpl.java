package tennisclub.facade;

import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisclub.dto.booking.BookingCreateDTO;
import tennisclub.dto.booking.BookingFullDTO;
import tennisclub.dto.booking.BookingUpdateDTO;
import tennisclub.dto.lesson.LessonFullDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.entity.Booking;
import tennisclub.entity.Court;
import tennisclub.entity.User;
import tennisclub.exceptions.FacadeLayerException;
import tennisclub.exceptions.ServiceLayerException;
import tennisclub.service.BookingService;
import tennisclub.service.CourtService;
import tennisclub.service.TimeService;
import tennisclub.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miroslav Demek
 */
@Service
@Transactional
public class BookingFacadeImpl implements BookingFacade {

    private final BookingService bookingService;
    private final CourtService courtService;
    private final UserService userService;

    final private Mapper mapper;

    @Autowired
    public BookingFacadeImpl(BookingService bookingService,
                             CourtService courtService,
                             UserService userService,
                             Mapper mapper) {
        this.bookingService = bookingService;
        this.courtService = courtService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public BookingFullDTO makeBooking(BookingCreateDTO createDTO) {
        Booking booking = mapper.map(createDTO, Booking.class);

        if (!courtService.isFree(booking.getCourt(), booking.getStartTime(), booking.getEndTime())) {
            throw new FacadeLayerException("Can't make a booking. Court is not free at this time.");
        }

        Duration totalTime = bookingService.getTotalReservedHoursToday(booking.getAuthor())
                .plus(Duration.between(booking.getStartTime(), booking.getEndTime()));
        if (totalTime.compareTo(Duration.ofHours(2)) > 0) {
            throw new FacadeLayerException("Maximum number of reserved hours per day exceeded.");
        }

        for (User user : booking.getUsers()) {
            if (booking.getAuthor().equals(user)) {
                throw new FacadeLayerException("Can't add author as a new booker.");
            }
        }

        Booking newBooking = bookingService.create(booking);

        return mapper.map(newBooking, BookingFullDTO.class);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingService.findById(bookingId);
        bookingService.remove(booking);
    }

    @Override
    public BookingFullDTO addUser(Long bookingId, Long userId) {
        Booking booking = bookingService.findById(bookingId);
        User user = userService.findUserById(userId);
        Booking updated = bookingService.addUser(booking, user);
        return mapper.map(updated, BookingFullDTO.class);
    }

    @Override
    public BookingFullDTO removeUser(Long bookingId, Long userId) {
        Booking booking = bookingService.findById(bookingId);
        User user = userService.findUserById(userId);
        Booking updated = bookingService.removeUser(booking, user);
        return mapper.map(updated, BookingFullDTO.class);
    }

    @Override
    public List<BookingFullDTO> findAll() {
        List<Booking> bookings = bookingService.findAll();
        return bookings.stream()
                .map(b -> mapper.map(b, BookingFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookingFullDTO findById(Long bookingId) {
        Booking booking = bookingService.findById(bookingId);
        return mapper.map(booking, BookingFullDTO.class);
    }

    @Override
    public List<BookingFullDTO> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        List<Booking> bookings = bookingService.findByTimeInterval(from ,to);
        return bookings.stream()
                .map(b -> mapper.map(b, BookingFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookingFullDTO update(Long bookingId, BookingUpdateDTO dto) {
        Booking booking = bookingService.findById(bookingId);
        booking.getUsers().clear();
        for (UserDTO userDto : dto.getUsers()) {
            User user = userService.findUserById(userDto.getId());
            if (booking.getAuthor().equals(user)) {
                throw new FacadeLayerException("Can't add author as a new booker.");
            }
            booking.addUser(user);
        }
        bookingService.update(booking);
        return mapper.map(booking, BookingFullDTO.class);
    }
}
