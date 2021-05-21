package tennisclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import tennisclub.ServiceTestsConfiguration;
import tennisclub.dao.BookingDao;
import tennisclub.entity.Booking;
import tennisclub.entity.Court;
import tennisclub.entity.User;
import tennisclub.exceptions.ServiceLayerException;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Xuan Linh Phamová
 */
@SpringBootTest
@ContextConfiguration(classes = ServiceTestsConfiguration.class)
public class BookingServiceTest {
    @MockBean
    private BookingDao bookingDao;

    @MockBean
    private TimeService timeService;

    @Autowired
    private BookingService bookingService;

    private Booking booking;

    private final LocalDate START_DAY = LocalDate.of(2048, 4,1);

    private final LocalTime START_TIME = LocalTime.MIN;

    // booking info
    private final Long BOOKING_ID = 1L;
    private final LocalDateTime BOOKING_START = LocalDateTime.of(START_DAY, START_TIME);
    private final LocalDateTime BOOKING_END = BOOKING_START.plusDays(1);

    private final LocalDateTime OTHER_START = BOOKING_START.plusDays(100);
    private final LocalDateTime OTHER_END = BOOKING_END.plusDays(100);

    private final Court COURT = new Court("Court 1");
    private final Court OTHER_COURT = new Court("Court 2");

    private User participant = new User();
    private User otherParticipant = new User();

    @BeforeEach
    public void setup(){
        booking = new Booking(BOOKING_START, BOOKING_END);
        booking.setId(BOOKING_ID);
        booking.setCourt(COURT);

        participant.setUsername("chibi");
        otherParticipant.setUsername("other chibi");
    }

    @Test
    public void testCreate(){
        bookingService.create(booking);
        verify(bookingDao).create(booking);
    }

    @Test
    public void testUpdate(){
        when(bookingDao.update(booking)).thenReturn(booking);
        booking.setCourt(OTHER_COURT);

        Booking updated = bookingService.update(booking);

        verify(bookingDao).update(booking);
        assertThat(updated).isEqualTo(booking);
    }

    @Test
    public void testRemove(){
        bookingService.remove(booking);
        verify(bookingDao).delete(booking);
    }

    @Test
    public void testRemoveUserOK(){
        when(timeService.getCurrentDateTime()).thenReturn(BOOKING_START);

        participant.addBooking(booking);
        booking.addUser(participant);

        otherParticipant.addBooking(booking);
        booking.addUser(otherParticipant);

        bookingService.removeUser(booking, participant);
        assertThat(participant.getBookings()).isEmpty();
    }

    @Test
    public void testRemoveUserThatIsNotAParticipant(){
        booking.addUser(participant);

        assertThatThrownBy(() -> bookingService.removeUser(booking, otherParticipant)).isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void testRemoveUserBookingWithoutParticipant(){
        participant.addBooking(booking);
        booking.addUser(participant);

        assertThatThrownBy(() -> bookingService.removeUser(booking, otherParticipant)).isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void testRemoveUserBookingWithOneParticipant(){
        assertThatThrownBy(() -> bookingService.removeUser(booking, otherParticipant)).isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void testRemoveUserBookingAfterEnrollmentPeriod(){
        when(timeService.getCurrentDateTime()).thenReturn(BOOKING_END.plusDays(1));

        assertThatThrownBy(() -> bookingService.removeUser(booking, otherParticipant)).isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void testAddUserOK(){
        when(timeService.getCurrentDateTime()).thenReturn(BOOKING_START);

        bookingService.addUser(booking, participant);
        assertThat(participant.getBookings()).contains(booking);
    }

    @Test
    public void testAddUserThatIsAlreadyAParticipant(){
        booking.addUser(participant);

        assertThatThrownBy(() -> bookingService.addUser(booking, participant)).isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void testAddUserBookingAfterEnrollmentPeriod(){
        when(timeService.getCurrentDateTime()).thenReturn(BOOKING_END.plusDays(1));

        assertThatThrownBy(() -> bookingService.addUser(booking, otherParticipant)).isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void testFindByIdBooking(){
        when(bookingDao.findById(BOOKING_ID)).thenReturn(booking);

        Booking found = bookingService.findById(BOOKING_ID);

        verify(bookingDao).findById(BOOKING_ID);
        assertThat(found).isEqualTo(booking);
    }


    @Test
    public void testFindByNonExistingId(){
        final Long NON_EXISTING_ID = 100L;
        when(bookingDao.findById(NON_EXISTING_ID)).thenReturn(null);

        Booking found = bookingService.findById(NON_EXISTING_ID);

        verify(bookingDao).findById(NON_EXISTING_ID);
        assertThat(found).isNull();
    }

    @Test
    public void testFindAll(){
        List<Booking> events = asList(booking);

        when(bookingDao.findAll()).thenReturn(events);

        List<Booking> found = bookingService.findAll();

        verify(bookingDao).findAll();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(booking);
    }

    @Test
    public void testFindAllEmpty(){
        List<Booking> events = Collections.emptyList();

        when(bookingDao.findAll()).thenReturn(events);

        List<Booking> found = bookingService.findAll();

        verify(bookingDao).findAll();
        assertThat(found).isEmpty();
    }

    @Test
    public void testFindByTimeIntervalBooking(){
        // only booking is in the time interval
        List<Booking> onlyBooking = Collections.singletonList(booking);

        when(bookingDao.findByTimeInterval(BOOKING_START, BOOKING_END)).thenReturn(onlyBooking);

        List<Booking> found = bookingService.findByTimeInterval(BOOKING_START, BOOKING_END);

        verify(bookingDao).findByTimeInterval(BOOKING_START, BOOKING_END);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(booking);
    }


    @Test
    public void testFindByTimeIntervalEmpty(){
        when(bookingDao.findByTimeInterval(OTHER_START, OTHER_END)).thenReturn(Collections.emptyList());

        List<Booking> found = bookingService.findByTimeInterval(OTHER_START, OTHER_END);

        verify(bookingDao).findByTimeInterval(OTHER_START, OTHER_END);
        assertThat(found).isEmpty();
    }
}
