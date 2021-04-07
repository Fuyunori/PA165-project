package tennisclub.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import tennisclub.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
public class BookingDaoTest {
    @Autowired
    private BookingDao bookingDao;

    @PersistenceContext
    private EntityManager em;

    // start time
    private LocalDate dateStart = LocalDate.of(2020,4,1);
    private LocalTime timeStart = LocalTime.of(0,0,0,0);
    private LocalDateTime eventStart = LocalDateTime.of(dateStart, timeStart);

    // end time
    private LocalDate dateEnd = LocalDate.of(2021,4,1);
    private LocalTime timeEnd = LocalTime.of(0,0,0,0);
    private LocalDateTime eventEnd = LocalDateTime.of(dateEnd, timeEnd);

    // tomorrow
    private LocalDateTime bookingStart = eventStart.plusDays(1);
    private LocalDateTime bookingEnd = eventEnd.plusDays(1);

    // day after tomorrow
    private final LocalDateTime lessonStart = eventStart.plusDays(2);
    private final LocalDateTime lessonEnd = eventEnd.plusDays(2);

    // 3 days later
    private final LocalDateTime tournamentStart = eventStart.plusDays(3);
    private final LocalDateTime tournamentEnd = eventEnd.plusDays(3);

    // other date
    private final LocalDateTime otherStart = eventStart.plusDays(10);
    private final LocalDateTime otherEnd = eventEnd.plusDays(10);

    private Court bookingCourt = new Court("Court 2");
    private Court otherCourt = new Court("Other court");

    private Booking booking = new Booking(bookingStart, bookingEnd);
    private Event tournament = new Tournament(tournamentStart, tournamentEnd, 10, 10000);


    @BeforeEach
    public void setup(){
        em.persist(bookingCourt);
        em.persist(otherCourt);

        booking.setCourt(bookingCourt);

        em.persist(booking);
    }

    @Test
    public void testBookingCreationOK(){
        Booking booking = new Booking(bookingStart, bookingEnd);
        booking.setCourt(bookingCourt);
        bookingDao.create(booking);

        // test that the created instance is in the database
        Booking foundEvent = em.find(Booking.class, booking.getId());
        assertThat(foundEvent.getStartTime()).isEqualTo(bookingStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(bookingEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(bookingCourt);
        assertThat(foundEvent).isEqualTo(booking);
    }


    @Test
    public void testBookingCreationWithoutCourt(){
        Booking booking = new Booking(bookingStart, bookingEnd);
        assertThatThrownBy(() -> bookingDao.create(booking)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testBookingUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        booking.setStartTime(otherStart);
        Booking updatedEvent = bookingDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(otherStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(bookingEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(bookingCourt);
    }

    @Test
    public void testBookingUpdatingStartTimeToNull(){
        booking.setStartTime(null);
        Event updatedEvent = bookingDao.update(booking);

        // check that the start time has remained the same
        assertThat(updatedEvent.getStartTime()).isEqualTo(booking.getStartTime());
    }

    @Test
    public void testBookingUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        booking.setEndTime(otherEnd);
        Event updatedEvent = bookingDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(bookingStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(otherEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(bookingCourt);
    }

    @Test
    public void testBookingUpdatingEndTimeToNull(){
        booking.setEndTime(null);
        Event updatedEvent = bookingDao.update(booking);

        // check that the start time has remained the same
        assertThat(updatedEvent.getEndTime()).isEqualTo(booking.getEndTime());
    }

    @Test
    public void testBookingUpdatingCourt(){
        // update the event - change court
        booking.setCourt(otherCourt);
        Event updatedEvent = bookingDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(bookingStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(bookingEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(otherCourt);
    }

    @Test
    public void testBookingUpdatingCourtToNull(){
        // update the event - change court
        booking.setCourt(null);
        Event updatedEvent = bookingDao.update(booking);

        // check that the start time has remained the same
        assertThat(updatedEvent.getCourt()).isEqualTo(booking.getCourt());
    }

    @Test
    public void testBookingDeleting(){
        // delete the event
        bookingDao.delete(booking);

        Event deletedEvent = em.find(Event.class, booking.getId());

        // not sure about this - in the court test, it should throw an exception
        assertThat(deletedEvent).isEqualTo(null);
    }

    @Test
    public void findBookingById(){
        Event foundEvent = bookingDao.findById(booking.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(bookingStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(bookingEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(bookingCourt);
        assertThat(foundEvent).isEqualTo(booking);
    }

    @Test
    public void findBookingsByCourtOfWhichThereAreTwoSharingTheSameCourt(){
        Booking otherBooking = new Booking(otherStart, otherEnd);
        otherBooking.setCourt(bookingCourt);
        em.persist(otherBooking);

        List<Booking> foundEvents = bookingDao.findByCourt(bookingCourt);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(otherBooking);
    }

    @Test
    public void findBookingsByCourtOfWhichThereAreTwoNotSharingTheSameCourt(){
        Booking otherBooking = new Booking(bookingStart, bookingEnd);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Booking> foundEvents = bookingDao.findByCourt(bookingCourt);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findBookingsByStartTimeOfWhichThereAreTwoSharingTheSameStartTime(){
        Booking otherBooking = new Booking(bookingStart, bookingEnd);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Booking> foundEvents = bookingDao.findByStartTime(bookingStart);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(otherBooking);
    }

    @Test
    public void findBookingsByStartTimeOfWhichThereAreTwoNotSharingTheSameStartTime(){
        Booking otherBooking = new Booking(otherStart, otherEnd);
        otherBooking.setCourt(bookingCourt);
        em.persist(otherBooking);

        List<Booking> foundEvents = bookingDao.findByStartTime(bookingStart);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findBookingsByEndTimeOfWhichThereAreTwoSharingTheSameEndTime(){
        Booking otherBooking = new Booking(bookingStart, bookingEnd);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Booking> foundEvents = bookingDao.findByEndTime(bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(otherBooking);
    }

    @Test
    public void findBookingsByEndTimeOfWhichThereAreTwoNotSharingTheSameEndTime(){
        Booking otherBooking = new Booking(otherStart, otherEnd);
        otherBooking.setCourt(bookingCourt);
        em.persist(otherBooking);

        List<Booking> foundEvents = bookingDao.findByEndTime(bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void testFindAll(){
        List<Booking> allEvents = bookingDao.findAll();

        assertThat(allEvents.size()).isEqualTo(1);
        assertThat(allEvents).contains(booking);
    }

    private LocalDateTime tomorrow(LocalDateTime dateTime){
        return dateTime.plusDays(1);
    }

    private LocalDateTime yesterday(LocalDateTime dateTime){
        return dateTime.minusDays(1);
    }

    @Test
    public void findBookingByTimeIntervalStartTimeIsEqualToTo(){
        Booking otherBooking = new Booking(bookingEnd, bookingEnd);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Booking> foundEvents = bookingDao.findByTimeInterval(bookingStart, bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findBookingByTimeIntervalStartTimeIsBiggerThanTo(){
        Booking otherBooking = new Booking(tomorrow(bookingEnd), tomorrow(bookingEnd));
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Booking> foundEvents = bookingDao.findByTimeInterval(bookingStart, bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findBookingByTimeIntervalEndTimeIsEqualToFrom(){
        Booking otherBooking = new Booking(bookingStart, bookingStart);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Booking> foundEvents = bookingDao.findByTimeInterval(bookingStart, bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findBookingByTimeIntervalEndTimeIsSmallerThanFrom(){
        Booking otherBooking = new Booking(yesterday(bookingStart), yesterday(bookingStart));
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Booking> foundEvents = bookingDao.findByTimeInterval(bookingStart, bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findBookingByTimeIntervalBothTimesAreOK(){
        Booking otherBooking = new Booking(bookingStart, bookingEnd);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Booking> foundEvents = bookingDao.findByTimeInterval(bookingStart, bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(otherBooking);
    }

}
