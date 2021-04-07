package tennisclub.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tennisclub.entity.*;
import tennisclub.entity.enums.Level;

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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class EventDaoTest {
    @Autowired
    private EventDao eventDao;

    @PersistenceContext
    private EntityManager em;

    // start time
    private LocalDate dateStart = LocalDate.of(2020,4,1);
    private LocalTime timeStart = LocalTime.of(0,0,0,0);
    private LocalDateTime start = LocalDateTime.of(dateStart, timeStart);

    // end time
    private LocalDate dateEnd = LocalDate.of(2021,4,1);
    private LocalTime timeEnd = LocalTime.of(0,0,0,0);
    private LocalDateTime end = LocalDateTime.of(dateEnd, timeEnd);

    // tomorrow
    private LocalDateTime tomorrowStart = start.plusDays(1);
    private LocalDateTime tomorrowEnd = end.plusDays(1);

    private final LocalDateTime twoDaysLaterStart = start.plusDays(2);
    private final LocalDateTime twoDaysLaterEnd = end.plusDays(2);
    private final LocalDateTime threeDaysLaterStart = start.plusDays(3);
    private final LocalDateTime threeDaysLaterEnd = end.plusDays(3);

    private Court court = new Court("Court 1");
    private Event event = new Event(start, end);
    private Event booking = new Booking(tomorrowStart, tomorrowEnd);
    private Event lesson = new Lesson(twoDaysLaterStart, twoDaysLaterEnd, Level.BEGINNER);
    private Event tournament = new Tournament(threeDaysLaterStart, threeDaysLaterEnd, 10, 10000);


    @BeforeEach
    public void setup(){
        em.persist(court);

        event.setCourt(court);
        booking.setCourt(court);
        lesson.setCourt(court);
        tournament.setCourt(court);

        em.persist(event);
        em.persist(booking);
        em.persist(lesson);
        em.persist(tournament);
    }

    @Test
    public void testEventCreation(){
        Event event = new Event(start, end);
        event.setCourt(court);
        eventDao.create(event);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, event.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(start);
        assertThat(foundEvent.getEndTime()).isEqualTo(end);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(event);
    }

    @Test
    public void testBookingCreation(){
        Event booking = new Booking(start, end);
        booking.setCourt(court);
        eventDao.create(booking);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, booking.getId());
        assertThat(foundEvent.getStartTime()).isEqualTo(start);
        assertThat(foundEvent.getEndTime()).isEqualTo(end);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(booking);
    }

    @Test
    public void testLessonCreation(){
        Event lesson = new Lesson(start, end, Level.BEGINNER);
        lesson.setCourt(court);
        eventDao.create(lesson);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, lesson.getId());
        assertThat(foundEvent.getStartTime()).isEqualTo(start);
        assertThat(foundEvent.getEndTime()).isEqualTo(end);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(lesson);
    }

    @Test
    public void testTournamentCreation(){
        Event tournament = new Tournament(start, end, 10, 10000);
        tournament.setCourt(court);
        eventDao.create(tournament);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, tournament.getId());
        assertThat(foundEvent.getStartTime()).isEqualTo(start);
        assertThat(foundEvent.getEndTime()).isEqualTo(end);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(tournament);
    }

    @Test
    public void testEventUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        event.setStartTime(start.plusDays(2));
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(start.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(end);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testEventUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        event.setEndTime(end.plusDays(2));
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(start);
        assertThat(updatedEvent.getEndTime()).isEqualTo(end.plusDays(2));
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testEventUpdatingCourt(){
        // update the event - change court
        Court anotherCourt = new Court("Court 2");
        em.persist(anotherCourt);

        event.setCourt(anotherCourt);
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(start);
        assertThat(updatedEvent.getEndTime()).isEqualTo(end);
        assertThat(updatedEvent.getCourt()).isEqualTo(anotherCourt);
    }

    @Test
    public void testBookingUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        booking.setStartTime(tomorrowStart.plusDays(2));
        Event updatedEvent = eventDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(tomorrowStart.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(tomorrowEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testBookingUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        booking.setEndTime(tomorrowEnd.plusDays(2));
        Event updatedEvent = eventDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(tomorrowStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(tomorrowEnd.plusDays(2));
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testBookingUpdatingCourt(){
        // update the event - change court
        Court anotherCourt = new Court("Court 2");
        em.persist(anotherCourt);

        booking.setCourt(anotherCourt);
        Event updatedEvent = eventDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(tomorrowStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(tomorrowEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(anotherCourt);
    }

    @Test
    public void testLessonUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        lesson.setStartTime(twoDaysLaterStart.plusDays(2));
        Event updatedEvent = eventDao.update(lesson);

        assertThat(updatedEvent.getStartTime()).isEqualTo(twoDaysLaterStart.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(twoDaysLaterEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testLessonUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        lesson.setEndTime(twoDaysLaterEnd.plusDays(2));
        Event updatedEvent = eventDao.update(lesson);

        assertThat(updatedEvent.getStartTime()).isEqualTo(twoDaysLaterStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(twoDaysLaterEnd.plusDays(2));
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testLessonUpdatingCourt(){
        // update the event - change court
        Court anotherCourt = new Court("Court 2");
        em.persist(anotherCourt);

        lesson.setCourt(anotherCourt);
        Event updatedEvent = eventDao.update(lesson);

        assertThat(updatedEvent.getStartTime()).isEqualTo(twoDaysLaterStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(twoDaysLaterEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(anotherCourt);
    }

    @Test
    public void testTournamentUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        tournament.setStartTime(threeDaysLaterStart.plusDays(2));
        Event updatedEvent = eventDao.update(tournament);

        assertThat(updatedEvent.getStartTime()).isEqualTo(threeDaysLaterStart.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(threeDaysLaterEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testTournamentUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        tournament.setEndTime(threeDaysLaterEnd.plusDays(2));
        Event updatedEvent = eventDao.update(tournament);

        assertThat(updatedEvent.getStartTime()).isEqualTo(threeDaysLaterStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(threeDaysLaterEnd.plusDays(2));
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testTournamentUpdatingCourt(){
        // update the event - change court
        Court anotherCourt = new Court("Court 2");
        em.persist(anotherCourt);

        tournament.setCourt(anotherCourt);
        Event updatedEvent = eventDao.update(tournament);

        assertThat(updatedEvent.getStartTime()).isEqualTo(threeDaysLaterStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(threeDaysLaterEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(anotherCourt);
    }

    @Test
    public void testEventDeleting(){
        // delete the event
        eventDao.delete(event);

        Event deletedEvent = em.find(Event.class, event.getId());

        // TODO - change if necessary
        // not sure about this - in the court test, it should throw an exception
        assertThat(deletedEvent).isEqualTo(null);
    }

    @Test
    public void testBookingDeleting(){
        // delete the event
        eventDao.delete(booking);

        Event deletedEvent = em.find(Event.class, booking.getId());

        // not sure about this - in the court test, it should throw an exception
        assertThat(deletedEvent).isEqualTo(null);
    }

    @Test
    public void testLessonDeleting(){
        // delete the event
        eventDao.delete(lesson);

        Event deletedEvent = em.find(Event.class, lesson.getId());

        // not sure about this - in the court test, it should throw an exception
        assertThat(deletedEvent).isEqualTo(null);
    }

    @Test
    public void testTournamentDeleting(){
        // delete the event
        eventDao.delete(tournament);

        Event deletedEvent = em.find(Event.class, tournament.getId());

        // not sure about this - in the court test, it should throw an exception
        assertThat(deletedEvent).isEqualTo(null);
    }


    @Test
    public void findEventById(){
        Event foundEvent = eventDao.findById(event.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(start);
        assertThat(foundEvent.getEndTime()).isEqualTo(end);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(event);
    }

    @Test
    public void findBookingById(){
        Event foundEvent = eventDao.findById(booking.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(tomorrowStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(tomorrowEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(booking);
    }

    @Test
    public void findLessonById(){
        Event foundEvent = eventDao.findById(lesson.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(twoDaysLaterStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(twoDaysLaterEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(lesson);
    }

    @Test
    public void findTournamentById(){
        Event foundEvent = eventDao.findById(tournament.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(threeDaysLaterStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(threeDaysLaterEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(tournament);
    }

    @Test
    public void findEventsByCourtOfWhichThereAreTwoSharingTheSameCourt(){
        Event otherEvent = new Event(start, end);
        otherEvent.setCourt(court);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByCourt(court);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).contains(otherEvent);
    }

    @Test
    public void findEventsByCourtOfWhichThereAreTwoNotSharingTheSameCourt(){
        Court otherCourt = new Court("Court 2");
        em.persist(otherCourt);

        Event otherEvent = new Event(start, end);
        otherEvent.setCourt(otherCourt);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByCourt(court);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).doesNotContain(otherEvent);
    }

    @Test
    public void findBookingsByCourtOfWhichThereAreTwoSharingTheSameCourt(){
        Event otherBooking = new Booking(start, end);
        otherBooking.setCourt(court);
        em.persist(otherBooking);

        List<Event> foundEvents = eventDao.findByCourt(court);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).contains(otherBooking);
    }

    @Test
    public void findBookingsByCourtOfWhichThereAreTwoNotSharingTheSameCourt(){
        Court otherCourt = new Court("Court 2");
        em.persist(otherCourt);

        Event otherBooking = new Event(start, end);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Event> foundEvents = eventDao.findByCourt(court);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findLessonsByCourtOfWhichThereAreTwoSharingTheSameCourt(){
        Event otherLesson = new Lesson(start, end, Level.BEGINNER);
        otherLesson.setCourt(court);
        em.persist(otherLesson);

        List<Event> foundEvents = eventDao.findByCourt(court);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).contains(otherLesson);
    }

    @Test
    public void findLessonsByCourtOfWhichThereAreTwoNotSharingTheSameCourt(){
        Court otherCourt = new Court("Court 2");
        em.persist(otherCourt);

        Event otherEvent = new Lesson(start, end, Level.BEGINNER);
        otherEvent.setCourt(otherCourt);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByCourt(court);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).doesNotContain(otherEvent);
    }

    @Test
    public void findTournamentsByCourtOfWhichThereAreTwoSharingTheSameCourt(){
        Event otherTournament = new Tournament(start, end, 10, 10000);
        otherTournament.setCourt(court);
        em.persist(otherTournament);

        List<Event> foundEvents = eventDao.findByCourt(court);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).contains(otherTournament);
    }

    @Test
    public void findTournamentsByCourtOfWhichThereAreTwoNotSharingTheSameCourt(){
        Court otherCourt = new Court("Court 2");
        em.persist(otherCourt);

        Event otherTournament = new Tournament(start, end, 10, 10000);
        otherTournament.setCourt(otherCourt);
        em.persist(otherTournament);

        List<Event> foundEvents = eventDao.findByCourt(court);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).doesNotContain(otherTournament);
    }
}
