package tennisclub.dao;

import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import tennisclub.entity.*;
import tennisclub.entity.enums.Level;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for Event.
 * @author Xuan Linh Phamová
 */
@SpringBootTest
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

    private Court eventCourt = new Court("Court 1");
    private Court bookingCourt = new Court("Court 2");
    private Court lessonCourt = new Court("Court 3");
    private Court tournamentCourt = new Court("Court 4");
    private Court otherCourt = new Court("Other court");

    private Event event = new Event(eventStart, eventEnd);
    private Event booking = new Booking(bookingStart, bookingEnd);
    private Event lesson = new Lesson(lessonStart, lessonEnd, Level.BEGINNER);
    private Event tournament = new Tournament(tournamentStart, tournamentEnd, 10, 10000);


    @BeforeEach
    public void setup(){
        em.persist(eventCourt);
        em.persist(bookingCourt);
        em.persist(lessonCourt);
        em.persist(tournamentCourt);
        em.persist(otherCourt);

        event.setCourt(eventCourt);
        booking.setCourt(bookingCourt);
        lesson.setCourt(lessonCourt);
        tournament.setCourt(tournamentCourt);

        em.persist(event);
        em.persist(booking);
        em.persist(lesson);
        em.persist(tournament);
    }

    @Test
    public void testEventCreationOK(){
        Event event = new Event(eventStart, eventEnd);
        event.setCourt(eventCourt);
        eventDao.create(event);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, event.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(eventStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(eventEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(eventCourt);
        assertThat(foundEvent).isEqualTo(event);
    }

    @Test
    public void testBookingCreationOK(){
        Event booking = new Booking(bookingStart, bookingEnd);
        booking.setCourt(bookingCourt);
        eventDao.create(booking);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, booking.getId());
        assertThat(foundEvent.getStartTime()).isEqualTo(bookingStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(bookingEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(bookingCourt);
        assertThat(foundEvent).isEqualTo(booking);
    }

    @Test
    public void testLessonCreationOK(){
        Event lesson = new Lesson(lessonStart, lessonEnd, Level.BEGINNER);
        lesson.setCourt(lessonCourt);
        eventDao.create(lesson);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, lesson.getId());
        assertThat(foundEvent.getStartTime()).isEqualTo(lessonStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(lessonEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(lessonCourt);
        assertThat(foundEvent).isEqualTo(lesson);
    }

    @Test
    public void testTournamentCreationOK(){
        Event tournament = new Tournament(tournamentStart, tournamentEnd, 10, 10000);
        tournament.setCourt(tournamentCourt);
        eventDao.create(tournament);

        // test that the created instance is in the database
        Event foundEvent = em.find(Event.class, tournament.getId());
        assertThat(foundEvent.getStartTime()).isEqualTo(tournamentStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(tournamentEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(tournamentCourt);
        assertThat(foundEvent).isEqualTo(tournament);
    }

    @Test
    public void testEventCreationWithoutCourt(){
        Event event = new Event(eventStart, eventEnd);
        assertThatThrownBy(() -> eventDao.create(event)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testBookingCreationWithoutCourt(){
        Event booking = new Booking(bookingStart, bookingEnd);
        assertThatThrownBy(() -> eventDao.create(booking)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testLessonCreationWithoutCourt(){
        Event lesson = new Lesson(lessonStart, lessonEnd, Level.BEGINNER);
        assertThatThrownBy(() -> eventDao.create(lesson)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testTournamentCreationWithoutCourt(){
        Event tournament = new Tournament(tournamentStart, tournamentEnd, 10, 10000);
        assertThatThrownBy(() -> eventDao.create(tournament)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testEventUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        event.setStartTime(eventStart.plusDays(2));
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(eventStart.plusDays(2));
        assertThat(updatedEvent.getEndTime()).isEqualTo(eventEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(eventCourt);
    }

    @Test
    public void testEventUpdatingStartTimeToNull(){
        event.setStartTime(null);
        Event updatedEvent = eventDao.update(event);

        assertThatThrownBy(() -> {
            eventDao.update(event);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testEventUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        event.setEndTime(eventEnd.plusDays(2));
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(eventStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(eventEnd.plusDays(2));
        assertThat(updatedEvent.getCourt()).isEqualTo(eventCourt);
    }

    @Test
    public void testEventUpdatingEndTimeToNull(){
        event.setEndTime(null);

        assertThatThrownBy(() -> {
            eventDao.update(event);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testEventUpdatingCourt(){
        // update the event - change court
        event.setCourt(otherCourt);
        Event updatedEvent = eventDao.update(event);

        assertThat(updatedEvent.getStartTime()).isEqualTo(eventStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(eventEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(otherCourt);
    }

    @Test
    public void testEventUpdatingCourtToNull(){
        // update the event - change court
        event.setCourt(null);

        assertThatThrownBy(() -> {
            eventDao.update(event);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testBookingUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        booking.setStartTime(otherStart);
        Event updatedEvent = eventDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(otherStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(bookingEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(bookingCourt);
    }

    @Test
    public void testBookingUpdatingStartTimeToNull(){
        booking.setStartTime(null);

        assertThatThrownBy(() -> {
            eventDao.update(booking);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testBookingUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        booking.setEndTime(otherEnd);
        Event updatedEvent = eventDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(bookingStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(otherEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(bookingCourt);
    }

    @Test
    public void testBookingUpdatingEndTimeToNull(){
        booking.setEndTime(null);

        assertThatThrownBy(() -> {
            eventDao.update(booking);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testBookingUpdatingCourt(){
        // update the event - change court
        booking.setCourt(otherCourt);
        Event updatedEvent = eventDao.update(booking);

        assertThat(updatedEvent.getStartTime()).isEqualTo(bookingStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(bookingEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(otherCourt);
    }

    @Test
    public void testBookingUpdatingCourtToNull(){
        // update the event - change court
        booking.setCourt(null);

        assertThatThrownBy(() -> {
            eventDao.update(booking);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testLessonUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        lesson.setStartTime(otherStart);
        Event updatedEvent = eventDao.update(lesson);

        assertThat(updatedEvent.getStartTime()).isEqualTo(otherStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(lessonEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(lessonCourt);
    }

    @Test
    public void testLessonUpdatingStartTimeToNull(){
        lesson.setStartTime(null);

        assertThatThrownBy(() -> {
            eventDao.update(lesson);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testLessonUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        lesson.setEndTime(otherEnd);
        Event updatedEvent = eventDao.update(lesson);

        assertThat(updatedEvent.getStartTime()).isEqualTo(lessonStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(otherEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(lessonCourt);
    }

    @Test
    public void testLessonUpdatingEndTimeToNull(){
        lesson.setEndTime(null);

        assertThatThrownBy(() -> {
            eventDao.update(lesson);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testLessonUpdatingCourt(){
        // update the event - change court
        lesson.setCourt(otherCourt);
        Event updatedEvent = eventDao.update(lesson);

        assertThat(updatedEvent.getStartTime()).isEqualTo(lessonStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(lessonEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(otherCourt);
    }

    @Test
    public void testLessonUpdatingCourtToNull(){
        // update the event - change court
        lesson.setCourt(null);

        assertThatThrownBy(() -> {
            eventDao.update(lesson);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testTournamentUpdatingStartTime(){
        // update the event - delay start time to 2 days later
        tournament.setStartTime(otherStart);
        Event updatedEvent = eventDao.update(tournament);

        assertThat(updatedEvent.getStartTime()).isEqualTo(otherStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(tournamentEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(tournamentCourt);
    }

    @Test
    public void testTournamentUpdatingStartTimeToNull(){
        tournament.setStartTime(null);

        assertThatThrownBy(() -> {
            eventDao.update(tournament);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testTournamentUpdatingEndTime(){
        // update the event - delay end time to 2 days later
        tournament.setEndTime(otherEnd);
        Event updatedEvent = eventDao.update(tournament);

        assertThat(updatedEvent.getStartTime()).isEqualTo(tournamentStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(otherEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(tournamentCourt);
    }

    @Test
    public void testTournamentUpdatingEndTimeToNull(){
        tournament.setEndTime(null);

        assertThatThrownBy(() -> {
            eventDao.update(tournament);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testTournamentUpdatingCourt(){
        // update the event - change court
        tournament.setCourt(otherCourt);
        Event updatedEvent = eventDao.update(tournament);

        assertThat(updatedEvent.getStartTime()).isEqualTo(tournamentStart);
        assertThat(updatedEvent.getEndTime()).isEqualTo(tournamentEnd);
        assertThat(updatedEvent.getCourt()).isEqualTo(otherCourt);
    }

    @Test
    public void testTournamentUpdatingCourtToNull(){
        // update the event - change court
        tournament.setCourt(null);

        assertThatThrownBy(() -> {
            eventDao.update(tournament);
            em.flush();
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void testEventDeleting(){
        // delete the event
        eventDao.delete(event);

        Event deletedEvent = em.find(Event.class, event.getId());

        assertThat(deletedEvent).isEqualTo(null);
    }

    @Test
    public void testDeleteAfterDetach() {
        em.detach(booking);
        eventDao.delete(booking);
        Event deletedEvent = em.find(Event.class, booking.getId());
        assertThat(deletedEvent).isEqualTo(null);
    }

    @Test
    public void testBookingDeleting(){
        // delete the event
        eventDao.delete(booking);

        Event deletedEvent = em.find(Event.class, booking.getId());

        assertThat(deletedEvent).isEqualTo(null);
    }

    @Test
    public void testLessonDeleting(){
        // delete the event
        eventDao.delete(lesson);

        Event deletedEvent = em.find(Event.class, lesson.getId());

        assertThat(deletedEvent).isEqualTo(null);
    }

    @Test
    public void testTournamentDeleting(){
        // delete the event
        eventDao.delete(tournament);

        Event deletedEvent = em.find(Event.class, tournament.getId());

        assertThat(deletedEvent).isEqualTo(null);
    }


    @Test
    public void findEventById(){
        Event foundEvent = eventDao.findById(event.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(eventStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(eventEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(eventCourt);
        assertThat(foundEvent).isEqualTo(event);
    }

    @Test
    public void findBookingById(){
        Event foundEvent = eventDao.findById(booking.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(bookingStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(bookingEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(bookingCourt);
        assertThat(foundEvent).isEqualTo(booking);
    }

    @Test
    public void findLessonById(){
        Event foundEvent = eventDao.findById(lesson.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(lessonStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(lessonEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(lessonCourt);
        assertThat(foundEvent).isEqualTo(lesson);
    }

    @Test
    public void findTournamentById(){
        Event foundEvent = eventDao.findById(tournament.getId());

        assertThat(foundEvent.getStartTime()).isEqualTo(tournamentStart);
        assertThat(foundEvent.getEndTime()).isEqualTo(tournamentEnd);
        assertThat(foundEvent.getCourt()).isEqualTo(tournamentCourt);
        assertThat(foundEvent).isEqualTo(tournament);
    }

    @Test
    public void findEventsByStartTimeOfWhichThereAreTwoSharingTheSameStartTime(){
        Event otherEvent = new Event(eventStart, eventEnd);
        otherEvent.setCourt(otherCourt);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByStartTime(eventStart);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(otherEvent);
    }

    @Test
    public void findEventsByStartTimeOfWhichThereAreTwoNotSharingTheSameStartTime(){
        Event otherEvent = new Event(otherStart, otherEnd);
        otherEvent.setCourt(eventCourt);
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
        Event otherBooking = new Booking(otherStart, otherEnd);
        otherBooking.setCourt(bookingCourt);
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
        otherLesson.setCourt(lessonCourt);
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
        otherTournament.setCourt(tournamentCourt);
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
        otherEvent.setCourt(eventCourt);
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
        Event otherBooking = new Booking(otherStart, otherEnd);
        otherBooking.setCourt(bookingCourt);
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
        otherLesson.setCourt(lessonCourt);
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
        otherTournament.setCourt(tournamentCourt);
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

    private LocalDateTime tomorrow(LocalDateTime dateTime){
        return dateTime.plusDays(1);
    }

    private LocalDateTime yesterday(LocalDateTime dateTime){
        return dateTime.minusDays(1);
    }

    @Test
    public void findEventByTimeIntervalStartTimeIsEqualToTo(){
        Event otherEvent = new Event(eventEnd, eventEnd);
        otherEvent.setCourt(otherCourt);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByTimeInterval(eventStart, eventEnd);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).doesNotContain(otherEvent);
    }

    @Test
    public void findEventByTimeIntervalStartTimeIsBiggerThanTo(){
        Event otherEvent = new Event(tomorrow(eventEnd), tomorrow(eventEnd));
        otherEvent.setCourt(otherCourt);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByTimeInterval(eventStart, eventEnd);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).doesNotContain(otherEvent);
    }

    @Test
    public void findEventByTimeIntervalEndTimeIsEqualToFrom(){
        Event otherEvent = new Event(eventStart, eventStart);
        otherEvent.setCourt(otherCourt);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByTimeInterval(eventStart, eventEnd);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).doesNotContain(otherEvent);
    }

    @Test
    public void findEventByTimeIntervalEndTimeIsSmallerThanFrom(){
        Event otherEvent = new Event(yesterday(eventStart), yesterday(eventStart));
        otherEvent.setCourt(otherCourt);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByTimeInterval(eventStart, eventEnd);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).doesNotContain(otherEvent);
    }

    @Test
    public void findEventByTimeIntervalBothTimesAreOK(){
        Event otherEvent = new Event(eventStart, eventEnd);
        otherEvent.setCourt(otherCourt);
        em.persist(otherEvent);

        List<Event> foundEvents = eventDao.findByTimeInterval(eventStart, eventEnd);

        assertThat(foundEvents).contains(event);
        assertThat(foundEvents).contains(otherEvent);
    }

    @Test
    public void findBookingByTimeIntervalStartTimeIsEqualToTo(){
        Event otherBooking = new Booking(bookingEnd, bookingEnd);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Event> foundEvents = eventDao.findByTimeInterval(bookingStart, bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findBookingByTimeIntervalStartTimeIsBiggerThanTo(){
        Event otherBooking = new Booking(tomorrow(bookingEnd), tomorrow(bookingEnd));
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Event> foundEvents = eventDao.findByTimeInterval(bookingStart, bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findBookingByTimeIntervalEndTimeIsEqualToFrom(){
        Event otherBooking = new Booking(bookingStart, bookingStart);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Event> foundEvents = eventDao.findByTimeInterval(bookingStart, bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findBookingByTimeIntervalEndTimeIsSmallerThanFrom(){
        Event otherBooking = new Booking(yesterday(bookingStart), yesterday(bookingStart));
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Event> foundEvents = eventDao.findByTimeInterval(bookingStart, bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).doesNotContain(otherBooking);
    }

    @Test
    public void findBookingByTimeIntervalBothTimesAreOK(){
        Event otherBooking = new Booking(bookingStart, bookingEnd);
        otherBooking.setCourt(otherCourt);
        em.persist(otherBooking);

        List<Event> foundEvents = eventDao.findByTimeInterval(bookingStart, bookingEnd);

        assertThat(foundEvents).contains(booking);
        assertThat(foundEvents).contains(otherBooking);
    }

    @Test
    public void findLessonByTimeIntervalStartTimeIsEqualToTo(){
        Event otherLesson = new Lesson(lessonEnd, lessonEnd, Level.BEGINNER);
        otherLesson.setCourt(otherCourt);
        em.persist(otherLesson);

        List<Event> foundEvents = eventDao.findByTimeInterval(lessonStart, lessonEnd);

        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).doesNotContain(otherLesson);
    }

    @Test
    public void findLessonByTimeIntervalStartTimeIsBiggerThanTo(){
        Event otherLesson = new Lesson(tomorrow(lessonEnd), tomorrow(lessonEnd), Level.BEGINNER);
        otherLesson.setCourt(otherCourt);
        em.persist(otherLesson);

        List<Event> foundEvents = eventDao.findByTimeInterval(lessonStart, lessonEnd);

        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).doesNotContain(otherLesson);
    }

    @Test
    public void findLessonByTimeIntervalEndTimeIsEqualToFrom(){
        Event otherLesson = new Lesson(lessonStart, lessonStart, Level.BEGINNER);
        otherLesson.setCourt(otherCourt);
        em.persist(otherLesson);

        List<Event> foundEvents = eventDao.findByTimeInterval(lessonStart, lessonEnd);

        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).doesNotContain(otherLesson);
    }

    @Test
    public void findLessonByTimeIntervalEndTimeIsSmallerThanFrom(){
        Event otherLesson = new Lesson(yesterday(lessonStart), yesterday(lessonStart), Level.BEGINNER);
        otherLesson.setCourt(otherCourt);
        em.persist(otherLesson);

        List<Event> foundEvents = eventDao.findByTimeInterval(lessonStart, lessonEnd);

        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).doesNotContain(otherLesson);
    }

    @Test
    public void findLessonByTimeIntervalBothTimesAreOK(){
        Event otherLesson = new Lesson(lessonStart, lessonEnd, Level.BEGINNER);
        otherLesson.setCourt(otherCourt);
        em.persist(otherLesson);

        List<Event> foundEvents = eventDao.findByTimeInterval(lessonStart, lessonEnd);

        assertThat(foundEvents).contains(lesson);
        assertThat(foundEvents).contains(otherLesson);
    }

    @Test
    public void findTournamentByTimeIntervalStartTimeIsEqualToTo(){
        Event otherTournament = new Tournament(tournamentEnd, tournamentEnd, 10, 10000);
        otherTournament.setCourt(otherCourt);
        em.persist(otherTournament);

        List<Event> foundEvents = eventDao.findByTimeInterval(tournamentStart, tournamentEnd);

        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).doesNotContain(otherTournament);
    }

    @Test
    public void findTournamentByTimeIntervalEndTimeIsEqualToFrom(){
        Event otherTournament = new Tournament(tournamentStart, tournamentStart, 10, 10000);
        otherTournament.setCourt(otherCourt);
        em.persist(otherTournament);

        List<Event> foundEvents = eventDao.findByTimeInterval(tournamentStart, tournamentEnd);

        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).doesNotContain(otherTournament);
    }

    @Test
    public void findTournamentByTimeIntervalBothTimesAreOK(){
        Event otherTournament = new Tournament(tournamentStart, tournamentEnd, 10, 10000);
        otherTournament.setCourt(otherCourt);
        em.persist(otherTournament);

        List<Event> foundEvents = eventDao.findByTimeInterval(tournamentStart, tournamentEnd);

        assertThat(foundEvents).contains(tournament);
        assertThat(foundEvents).contains(otherTournament);
    }
}
