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

    Court court = new Court("Court 1");

    // shouldn't be @BeforeEach, but now too lazy to change it to BeforeClass
    @BeforeEach
    public void setup(){
        em.persist(court);
    }

    @Test
    public void testEventCreation(){
        Event event = new Event(start, end);
        event.setCourt(court);
        eventDao.create(event);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, event.getId());
        assertThat(foundEvent).isEqualTo(event);
    }

    @Test
    public void testBookingCreation(){
        Event event = new Booking(start, end);
        event.setCourt(court);
        eventDao.create(event);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, event.getId());
        assertThat(foundEvent).isEqualTo(event);
    }

    @Test
    public void testLessonCreation(){
        Event event = new Lesson(start, end, Level.BEGINNER, 10);
        event.setCourt(court);
        eventDao.create(event);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, event.getId());
        assertThat(foundEvent).isEqualTo(event);
    }

    @Test
    public void testTournamentCreation(){
        Event event = new Tournament(start, end, 10, 10000);
        event.setCourt(court);
        eventDao.create(event);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, event.getId());
        assertThat(foundEvent).isEqualTo(event);
    }

    @Test
    public void testEventUpdating(){
        Event event = new Event(start, end);
        event.setCourt(court);
        em.persist(event);

        assertThat(event.getStartTime()).isEqualTo(start);
        assertThat(event.getEndTime()).isEqualTo(end);

        // update the event - delay start time to 2 days later
        event.setStartTime(start.plusDays(2));
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(start.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(end);
    }

    @Test
    public void testBookingUpdating(){
        Event event = new Booking(start, end);
        event.setCourt(court);
        em.persist(event);

        assertThat(event.getStartTime()).isEqualTo(start);
        assertThat(event.getEndTime()).isEqualTo(end);

        // update the event - delay start time to 2 days later
        event.setStartTime(start.plusDays(2));
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(start.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(end);
    }

    @Test
    public void testLessonUpdating(){
        Event event = new Lesson(start, end, Level.BEGINNER, 10);
        event.setCourt(court);
        em.persist(event);

        assertThat(event.getStartTime()).isEqualTo(start);
        assertThat(event.getEndTime()).isEqualTo(end);

        // update the event - delay start time to 2 days later
        event.setStartTime(start.plusDays(2));
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(start.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(end);
    }

    @Test
    public void testTournamentUpdating(){
        Event event = new Tournament(start, end, 10, 10000);
        event.setCourt(court);
        em.persist(event);

        assertThat(event.getStartTime()).isEqualTo(start);
        assertThat(event.getEndTime()).isEqualTo(end);

        // update the event - delay start time to 2 days later
        event.setStartTime(start.plusDays(2));
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(start.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(end);
    }

    @Test
    public void testEventDeleting(){
        Event event = new Event(start, end);
        event.setCourt(court);
        em.persist(event);

        Event foundEvent = em.find(Event.class, event.getId());
        assertThat(foundEvent).isEqualTo(event);

        // delete the event
        eventDao.delete(event);

        Event deletedEvent = em.find(Event.class, event.getId());

        // TODO - change if necessary
        // not sure about this - in the court test, it should throw an exception
        assertThat(deletedEvent).isEqualTo(null);
    }

    @Test
    public void testBookingDeleting(){
        Event event = new Booking(start, end);
        event.setCourt(court);
        em.persist(event);

        Event foundEvent = em.find(Event.class, event.getId());
        assertThat(foundEvent).isEqualTo(event);

        // delete the event
        eventDao.delete(event);

        Event deletedEvent = em.find(Event.class, event.getId());

        // not sure about this - in the court test, it should throw an exception
        assertThat(deletedEvent).isEqualTo(null);
    }

    @Test
    public void testLessonDeleting(){
        Event event = new Lesson(start, end, Level.BEGINNER, 10);
        event.setCourt(court);
        em.persist(event);

        Event foundEvent = em.find(Event.class, event.getId());
        assertThat(foundEvent).isEqualTo(event);

        // delete the event
        eventDao.delete(event);

        Event deletedEvent = em.find(Event.class, event.getId());

        // not sure about this - in the court test, it should throw an exception
        assertThat(deletedEvent).isEqualTo(null);
    }

    @Test
    public void testTournamentDeleting(){
        Event event = new Tournament(start, end, 10, 10000);
        event.setCourt(court);
        em.persist(event);

        Event foundEvent = em.find(Event.class, event.getId());
        assertThat(foundEvent).isEqualTo(event);

        // delete the event
        eventDao.delete(event);

        Event deletedEvent = em.find(Event.class, event.getId());

        // not sure about this - in the court test, it should throw an exception
        assertThat(deletedEvent).isEqualTo(null);
    }
}
