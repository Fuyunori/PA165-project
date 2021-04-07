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

    private Court court = new Court("Court 1");
    private Court otherCourt = new Court("Court 2");

    private Event event = new Event(eventStart, eventEnd);
    private Event booking = new Booking(bookingStart, bookingEnd);
    private Event lesson = new Lesson(lessonStart, lessonEnd, Level.BEGINNER);
    private Event tournament = new Tournament(tournamentStart, tournamentEnd, 10, 10000);


    @BeforeEach
    public void setup(){
        em.persist(court);
        em.persist(otherCourt);

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
        Event event = new Event(eventStart, eventEnd);
        event.setCourt(court);
        eventDao.create(event);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, event.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(eventStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(eventEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(event);
    }

    @Test
    public void testBookingCreation(){
        Event booking = new Booking(eventStart, eventEnd);
        booking.setCourt(court);
        eventDao.create(booking);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, booking.getId());
        assertThat(foundEvent.getStartTime()).isEqualTo(eventStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(eventEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(booking);
    }

    @Test
    public void testLessonCreation(){
        Event lesson = new Lesson(eventStart, eventEnd, Level.BEGINNER);
        lesson.setCourt(court);
        eventDao.create(lesson);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, lesson.getId());
        assertThat(foundEvent.getStartTime()).isEqualTo(eventStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(eventEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(lesson);
    }

    @Test
    public void testTournamentCreation(){
        Event tournament = new Tournament(eventStart, eventEnd, 10, 10000);
        tournament.setCourt(court);
        eventDao.create(tournament);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, tournament.getId());
        assertThat(foundEvent.getStartTime()).isEqualTo(eventStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(eventEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(tournament);
    }

    @Test
    public void testEventUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        event.setStartTime(eventStart.plusDays(2));
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(eventStart.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(eventEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testEventUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        event.setEndTime(eventEnd.plusDays(2));
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(eventStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(eventEnd.plusDays(2));
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testEventUpdatingCourt(){
        // update the event - change court
        Court anotherCourt = new Court("Court 2");
        em.persist(anotherCourt);

        event.setCourt(anotherCourt);
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(eventStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(eventEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(anotherCourt);
    }

    @Test
    public void testBookingUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        booking.setStartTime(bookingStart.plusDays(2));
        Event updatedEvent = eventDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(bookingStart.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(bookingEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testBookingUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        booking.setEndTime(bookingEnd.plusDays(2));
        Event updatedEvent = eventDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(bookingStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(bookingEnd.plusDays(2));
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testBookingUpdatingCourt(){
        // update the event - change court
        Court anotherCourt = new Court("Court 2");
        em.persist(anotherCourt);

        booking.setCourt(anotherCourt);
        Event updatedEvent = eventDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(bookingStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(bookingEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(anotherCourt);
    }

    @Test
    public void testLessonUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        lesson.setStartTime(lessonStart.plusDays(2));
        Event updatedEvent = eventDao.update(lesson);

        assertThat(updatedEvent.getStartTime()).isEqualTo(lessonStart.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(lessonEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testLessonUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        lesson.setEndTime(lessonEnd.plusDays(2));
        Event updatedEvent = eventDao.update(lesson);

        assertThat(updatedEvent.getStartTime()).isEqualTo(lessonStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(lessonEnd.plusDays(2));
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testLessonUpdatingCourt(){
        // update the event - change court
        Court anotherCourt = new Court("Court 2");
        em.persist(anotherCourt);

        lesson.setCourt(anotherCourt);
        Event updatedEvent = eventDao.update(lesson);

        assertThat(updatedEvent.getStartTime()).isEqualTo(lessonStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(lessonEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(anotherCourt);
    }

    @Test
    public void testTournamentUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        tournament.setStartTime(tournamentStart.plusDays(2));
        Event updatedEvent = eventDao.update(tournament);

        assertThat(updatedEvent.getStartTime()).isEqualTo(tournamentStart.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(tournamentEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testTournamentUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        tournament.setEndTime(tournamentEnd.plusDays(2));
        Event updatedEvent = eventDao.update(tournament);

        assertThat(updatedEvent.getStartTime()).isEqualTo(tournamentStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(tournamentEnd.plusDays(2));
        assertThat(updatedEvent.getCourt()).isEqualTo(court);
    }

    @Test
    public void testTournamentUpdatingCourt(){
        // update the event - change court
        Court anotherCourt = new Court("Court 2");
        em.persist(anotherCourt);

        tournament.setCourt(anotherCourt);
        Event updatedEvent = eventDao.update(tournament);

        assertThat(updatedEvent.getStartTime()).isEqualTo(tournamentStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(tournamentEnd);
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

        assertThat(foundEvent.getStartTime()).isEqualTo(eventStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(eventEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(event);
    }

    @Test
    public void findBookingById(){
        Event foundEvent = eventDao.findById(booking.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(bookingStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(bookingEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(booking);
    }

    @Test
    public void findLessonById(){
        Event foundEvent = eventDao.findById(lesson.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(lessonStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(lessonEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(lesson);
    }

    @Test
    public void findTournamentById(){
        Event foundEvent = eventDao.findById(tournament.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(tournamentStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(tournamentEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(court);
        assertThat(foundEvent).isEqualTo(tournament);
    }

    @Test
    public void findEventsByCourtOfWhichThereAreTwoSharingTheSameCourt(){
        Event otherEvent = new Event(eventStart, eventEnd);
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
        Event otherEvent = new Event(eventStart, eventEnd);
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
        Event otherBooking = new Booking(eventStart, eventEnd);
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
        Event otherBooking = new Event(eventStart, eventEnd);
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
        Event otherLesson = new Lesson(eventStart, eventEnd, Level.BEGINNER);
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
        Event otherLesson = new Lesson(eventStart, eventEnd, Level.BEGINNER);
        otherLesson.setCourt(otherCourt);
        em.persist(otherLesson);

        List<Event> foundEvents = eventDao.findByCourt(court);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).doesNotContain(otherLesson);
    }

    @Test
    public void findTournamentsByCourtOfWhichThereAreTwoSharingTheSameCourt(){
        Event otherTournament = new Tournament(eventStart, eventEnd, 10, 10000);
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
        Event otherTournament = new Tournament(eventStart, eventEnd, 10, 10000);
        otherTournament.setCourt(otherCourt);
        em.persist(otherTournament);

        List<Event> foundEvents = eventDao.findByCourt(court);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).doesNotContain(otherTournament);
    }

    @Test
    public void findEventsByStartTimeOfWhichThereAreTwoSharingTheSameStartTime(){
        Event otherEvent = new Event(eventStart, bookingStart);
        otherEvent.setCourt(otherCourt);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByStartTime(eventStart);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(otherEvent);
    }

    @Test
    public void findEventsByStartTimeOfWhichThereAreTwoNotSharingTheSameStartTime(){
        Event otherEvent = new Event(otherStart, otherEnd);
        otherEvent.setCourt(court);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByStartTime(eventStart);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).doesNotContain(otherEvent);
    }

    @Test
    public void findBookingsByStartTimeOfWhichThereAreTwoSharingTheSameStartTime(){
        Event otherBooking = new Booking(bookingStart, bookingEnd);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Event> foundEvents = eventDao.findByStartTime(bookingStart);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(otherBooking);
    }

    @Test
    public void findBookingsByStartTimeOfWhichThereAreTwoNotSharingTheSameStartTime(){
        Event otherBooking = new Event(otherStart, otherEnd);
        otherBooking.setCourt(court);
        em.persist(otherBooking);

        List<Event> foundEvents = eventDao.findByStartTime(bookingStart);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findLessonsByStartTimeOfWhichThereAreTwoSharingTheSameStartTime(){
        Event otherLesson = new Lesson(lessonStart, lessonEnd, Level.BEGINNER);
        otherLesson.setCourt(otherCourt);
        em.persist(otherLesson);

        List<Event> foundEvents = eventDao.findByStartTime(lessonStart);

        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(otherLesson);
    }

    @Test
    public void findLessonsByStartTimeOfWhichThereAreTwoNotSharingTheSameStartTime(){
        Event otherLesson = new Lesson(otherStart, otherEnd, Level.BEGINNER);
        otherLesson.setCourt(court);
        em.persist(otherLesson);

        List<Event> foundEvents = eventDao.findByStartTime(lessonStart);

        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).doesNotContain(otherLesson);
    }

    @Test
    public void findTournamentsByStartTimeOfWhichThereAreTwoSharingTheSameStartTime(){
        Event otherTournament = new Tournament(tournamentStart, tournamentEnd, 10, 10000);
        otherTournament.setCourt(otherCourt);
        em.persist(otherTournament);

        List<Event> foundEvents = eventDao.findByStartTime(tournamentStart);

        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).contains(otherTournament);
    }

    @Test
    public void findTournamentsByStartTimeOfWhichThereAreTwoNotSharingTheSameStartTime(){
        Event otherTournament = new Tournament(otherStart, otherEnd, 10, 10000);
        otherTournament.setCourt(court);
        em.persist(otherTournament);

        List<Event> foundEvents = eventDao.findByStartTime(tournamentStart);

        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).doesNotContain(otherTournament);
    }

    @Test
    public void findEventsByEndTimeOfWhichThereAreTwoSharingTheSameEndTime(){
        Event otherEvent = new Event(eventStart, eventEnd);
        otherEvent.setCourt(otherCourt);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByEndTime(eventEnd);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(otherEvent);
    }

    @Test
    public void findEventsByEndTimeOfWhichThereAreTwoNotSharingTheSameEndTime(){
        Event otherEvent = new Event(otherStart, otherEnd);
        otherEvent.setCourt(court);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByEndTime(eventEnd);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).doesNotContain(otherEvent);
    }

    @Test
    public void findBookingsByEndTimeOfWhichThereAreTwoSharingTheSameEndTime(){
        Event otherBooking = new Booking(bookingStart, bookingEnd);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Event> foundEvents = eventDao.findByEndTime(bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(otherBooking);
    }

    @Test
    public void findBookingsByEndTimeOfWhichThereAreTwoNotSharingTheSameEndTime(){
        Event otherBooking = new Event(otherStart, otherEnd);
        otherBooking.setCourt(court);
        em.persist(otherBooking);

        List<Event> foundEvents = eventDao.findByEndTime(bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findLessonsByEndTimeOfWhichThereAreTwoSharingTheSameEndTime(){
        Event otherLesson = new Lesson(lessonStart, lessonEnd, Level.BEGINNER);
        otherLesson.setCourt(otherCourt);
        em.persist(otherLesson);

        List<Event> foundEvents = eventDao.findByEndTime(lessonEnd);

        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(otherLesson);
    }

    @Test
    public void findLessonsByEndTimeOfWhichThereAreTwoNotSharingTheSameEndTime(){
        Event otherLesson = new Lesson(otherStart, otherEnd, Level.BEGINNER);
        otherLesson.setCourt(court);
        em.persist(otherLesson);

        List<Event> foundEvents = eventDao.findByEndTime(lessonEnd);

        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).doesNotContain(otherLesson);
    }

    @Test
    public void findTournamentsByEndTimeOfWhichThereAreTwoSharingTheSameEndTime(){
        Event otherTournament = new Tournament(tournamentStart, tournamentEnd, 10, 10000);
        otherTournament.setCourt(otherCourt);
        em.persist(otherTournament);

        List<Event> foundEvents = eventDao.findByEndTime(tournamentEnd);

        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).contains(otherTournament);
    }

    @Test
    public void findTournamentsByEndTimeOfWhichThereAreTwoNotSharingTheSameEndTime(){
        Event otherTournament = new Tournament(otherStart, otherEnd, 10, 10000);
        otherTournament.setCourt(court);
        em.persist(otherTournament);

        List<Event> foundEvents = eventDao.findByEndTime(tournamentEnd);

        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).doesNotContain(otherTournament);
    }

    @Test
    public void testFindAll(){
        List<Event> allEvents = eventDao.findAll();

        assertThat(allEvents.size()).isEqualTo(4);
        assertThat(allEvents).contains(event);
        assertThat(allEvents).contains(booking);
        assertThat(allEvents).contains(lesson);
        assertThat(allEvents).contains(tournament);
    }
}
