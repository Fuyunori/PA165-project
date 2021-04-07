package tennisclub.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import tennisclub.entity.*;
import tennisclub.entity.enums.Level;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    private Court court = new Court("Court 1");
    private Event event = new Event(start, end);
    private Event booking = new Booking(start, end);
    private Event lesson = new Lesson(start, end, Level.BEGINNER, 10);
    private Event tournament = new Tournament(start, end, 10, 10000);


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
        Event lesson = new Lesson(start, end, Level.BEGINNER, 10);
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
    public void testEventUpdating(){
        // update the event - delay start time to 2 days later
        event.setStartTime(start.plusDays(2));
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(start.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(end);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testBookingUpdating(){
        // update the event - delay start time to 2 days later
        booking.setStartTime(start.plusDays(2));
        Event updatedEvent = eventDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(start.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(end);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testLessonUpdating(){
        // update the event - delay start time to 2 days later
        lesson.setStartTime(start.plusDays(2));
        Event updatedEvent = eventDao.update(lesson);

        assertThat(updatedEvent.getStartTime()).isEqualTo(start.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(end);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testTournamentUpdating(){
        // update the event - delay start time to 2 days later
        tournament.setStartTime(start.plusDays(2));
        Event updatedEvent = eventDao.update(tournament);

        assertThat(updatedEvent.getStartTime()).isEqualTo(start.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(end);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
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

        assertThat(foundEvent.getStartTime()).isEqualTo(start);
        assertThat(foundEvent.getEndTime()).isEqualTo(end);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(booking);
    }

    @Test
    public void findLessonById(){
        Event foundEvent = eventDao.findById(lesson.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(start);
        assertThat(foundEvent.getEndTime()).isEqualTo(end);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(lesson);
    }

    @Test
    public void findTournamentById(){
        Event foundEvent = eventDao.findById(tournament.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(start);
        assertThat(foundEvent.getEndTime()).isEqualTo(end);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(tournament);
    }
}
