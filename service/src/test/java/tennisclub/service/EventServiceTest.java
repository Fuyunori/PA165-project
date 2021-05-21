package tennisclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import tennisclub.ServiceTestsConfiguration;
import tennisclub.dao.EventDao;
import tennisclub.entity.*;
import tennisclub.enums.Level;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Xuan Linh Phamová
 */
@SpringBootTest
@ContextConfiguration(classes = ServiceTestsConfiguration.class)
public class EventServiceTest {
    @MockBean
    private EventDao eventDao;

    @Autowired
    private EventService eventService;

    private Event booking;
    private Event lesson;
    private Event tournament;

    private final LocalDate START_DAY = LocalDate.of(2048, 4,1);

    private final LocalTime START_TIME = LocalTime.MIN;

    // time interval, in which all of the three event below are included
    private final LocalDateTime START = LocalDateTime.of(START_DAY, START_TIME);

    // booking info
    private final Long BOOKING_ID = 1L;
    private final LocalDateTime BOOKING_START = START;
    private final LocalDateTime BOOKING_END = START.plusDays(1);

    // lesson info
    private final Long LESSON_ID = 2L;
    private final LocalDateTime LESSON_START = BOOKING_END;
    private final LocalDateTime LESSON_END = LESSON_START.plusDays(1);

    // tournament info
    private final Long TOURNAMENT_ID = 3L;
    private final LocalDateTime TOURNAMENT_START = LESSON_END;
    private final LocalDateTime TOURNAMENT_END = TOURNAMENT_START.plusDays(1);

    private final LocalDateTime END = TOURNAMENT_END;

    private final LocalDateTime OTHER_START = START.plusDays(100);
    private final LocalDateTime OTHER_END = END.plusDays(100);

    private final Court COURT_1 = new Court("Court 1");
    private final Court COURT_2 = new Court("Court 2");
    private final Court COURT_3 = new Court("Court 2");

    @BeforeEach
    public void setup(){
        booking = new Booking(BOOKING_START, BOOKING_END);
        booking.setId(BOOKING_ID);
        booking.setCourt(COURT_1);

        lesson = new Lesson(LESSON_START, LESSON_END, Level.BEGINNER);
        lesson.setId(LESSON_ID);
        lesson.setCourt(COURT_2);

        tournament = new Tournament(TOURNAMENT_START, TOURNAMENT_END, "Wimbledon", 10, 10);
        tournament.setId(TOURNAMENT_ID);
        tournament.setCourt(COURT_3);
    }

    @Test
    public void testRescheduleBooking(){
        when(eventDao.update(booking)).thenReturn(booking);

        Event rescheduled = eventService.reschedule(booking, OTHER_START, OTHER_END);

        assertThat(rescheduled).isEqualTo(booking);
    }

    @Test
    public void testRescheduleLesson(){
        when(eventDao.update(lesson)).thenReturn(lesson);

        Event rescheduled = eventService.reschedule(lesson, OTHER_START, OTHER_END);

        assertThat(rescheduled).isEqualTo(lesson);
    }

    @Test
    public void testRescheduleTournament(){
        when(eventDao.update(tournament)).thenReturn(tournament);

        Event rescheduled = eventService.reschedule(tournament, OTHER_START, OTHER_END);

        assertThat(rescheduled).isEqualTo(tournament);
    }

    @Test
    public void testFindByIdBooking(){
        when(eventDao.findById(BOOKING_ID)).thenReturn(booking);

        Event found = eventService.findById(BOOKING_ID);

        verify(eventDao).findById(BOOKING_ID);
        assertThat(found).isEqualTo(booking);
    }

    @Test
    public void testFindByIdLesson(){
        when(eventDao.findById(LESSON_ID)).thenReturn(lesson);

        Event found = eventService.findById(LESSON_ID);

        verify(eventDao).findById(LESSON_ID);
        assertThat(found).isEqualTo(lesson);
    }

    @Test
    public void testFindByIdTournament(){
        when(eventDao.findById(TOURNAMENT_ID)).thenReturn(tournament);

        Event found = eventService.findById(TOURNAMENT_ID);

        verify(eventDao).findById(TOURNAMENT_ID);
        assertThat(found).isEqualTo(tournament);
    }

    @Test
    public void testFindByNonExistingId(){
        final Long NON_EXISTING_ID = 100L;
        when(eventDao.findById(NON_EXISTING_ID)).thenReturn(null);

        Event found = eventService.findById(NON_EXISTING_ID);

        verify(eventDao).findById(NON_EXISTING_ID);
        assertThat(found).isNull();
    }

    @Test
    public void testFindAll(){
        List<Event> events = asList(booking, lesson, tournament);

        when(eventDao.findAll()).thenReturn(events);

        List<Event> found = eventService.findAll();

        verify(eventDao).findAll();
        assertThat(found.size()).isEqualTo(3);
        assertThat(found).contains(booking);
        assertThat(found).contains(lesson);
        assertThat(found).contains(tournament);
    }

    @Test
    public void testFindAllEmpty(){
        List<Event> events = Collections.emptyList();

        when(eventDao.findAll()).thenReturn(events);

        List<Event> found = eventService.findAll();

        verify(eventDao).findAll();
        assertThat(found).isEmpty();
    }

    @Test
    public void testFindByTimeIntervalAll(){
        // all events start at the same time but are on different court
        List<Event> eventsWithSameTime = asList(booking, lesson, tournament);

        when(eventDao.findByTimeInterval(START, END)).thenReturn(eventsWithSameTime);

        List<Event> found = eventService.findByTimeInterval(START, END);

        verify(eventDao).findByTimeInterval(START, END);
        assertThat(found.size()).isEqualTo(3);
        assertThat(found).contains(booking);
        assertThat(found).contains(lesson);
        assertThat(found).contains(tournament);
    }

    @Test
    public void testFindByTimeIntervalBooking(){
        // only booking is in the time interval
        List<Event> onlyBooking = Collections.singletonList(booking);

        when(eventDao.findByTimeInterval(BOOKING_START, BOOKING_END)).thenReturn(onlyBooking);

        List<Event> found = eventService.findByTimeInterval(BOOKING_START, BOOKING_END);

        verify(eventDao).findByTimeInterval(BOOKING_START, BOOKING_END);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(booking);
    }

    @Test
    public void testFindByTimeIntervalLesson(){
        // only lesson is in the time interval
        List<Event> onlyLesson = Collections.singletonList(lesson);

        when(eventDao.findByTimeInterval(LESSON_START, LESSON_END)).thenReturn(onlyLesson);

        List<Event> found = eventService.findByTimeInterval(LESSON_START, LESSON_END);

        verify(eventDao).findByTimeInterval(LESSON_START, LESSON_END);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void testFindByTimeIntervalTournament(){
        // only booking is in the time interval
        List<Event> onlyTournament = Collections.singletonList(tournament);

        when(eventDao.findByTimeInterval(TOURNAMENT_START, TOURNAMENT_END)).thenReturn(onlyTournament);

        List<Event> found = eventService.findByTimeInterval(TOURNAMENT_START, TOURNAMENT_END);

        verify(eventDao).findByTimeInterval(TOURNAMENT_START, TOURNAMENT_END);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournament);
    }

    @Test
    public void testFindByTimeIntervalEmpty(){
        when(eventDao.findByTimeInterval(OTHER_START, OTHER_END)).thenReturn(Collections.emptyList());

        List<Event> found = eventService.findByTimeInterval(OTHER_START, OTHER_END);

        verify(eventDao).findByTimeInterval(OTHER_START, OTHER_END);
        assertThat(found).isEmpty();
    }

    @Test
    public void testFindByStartTimeBooking(){
        List<Event> onlyBooking = Collections.singletonList(booking);

        when(eventDao.findByStartTime(BOOKING_START)).thenReturn(onlyBooking);

        List<Event> found = eventService.findByStartTime(BOOKING_START);

        verify(eventDao).findByStartTime(BOOKING_START);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(booking);
    }

    @Test
    public void testFindByStartTimeLesson(){
        List<Event> onlyLesson = Collections.singletonList(lesson);

        when(eventDao.findByStartTime(LESSON_START)).thenReturn(onlyLesson);

        List<Event> found = eventService.findByStartTime(LESSON_START);

        verify(eventDao).findByStartTime(LESSON_START);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void testFindByStartTimeTournament(){
        List<Event> onlyTournament = Collections.singletonList(tournament);

        when(eventDao.findByStartTime(TOURNAMENT_START)).thenReturn(onlyTournament);

        List<Event> found = eventService.findByStartTime(TOURNAMENT_START);

        verify(eventDao).findByStartTime(TOURNAMENT_START);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournament);
    }

    @Test
    public void testFindByStartTimeEmpty(){
        when(eventDao.findByStartTime(OTHER_START)).thenReturn(Collections.emptyList());

        List<Event> found = eventService.findByStartTime(OTHER_START);

        verify(eventDao).findByStartTime(OTHER_START);
        assertThat(found).isEmpty();
    }

    @Test
    public void testFindByEndTimeBooking(){
        List<Event> onlyBooking = Collections.singletonList(booking);

        when(eventDao.findByEndTime(BOOKING_END)).thenReturn(onlyBooking);

        List<Event> found = eventService.findByEndTime(BOOKING_END);

        verify(eventDao).findByEndTime(BOOKING_END);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(booking);
    }

    @Test
    public void testFindByEndTimeLesson(){
        List<Event> onlyLesson = Collections.singletonList(lesson);

        when(eventDao.findByEndTime(LESSON_END)).thenReturn(onlyLesson);

        List<Event> found = eventService.findByEndTime(LESSON_END);

        verify(eventDao).findByEndTime(LESSON_END);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void testFindByEndTimeTournament(){
        List<Event> onlyTournament = Collections.singletonList(tournament);

        when(eventDao.findByEndTime(TOURNAMENT_END)).thenReturn(onlyTournament);

        List<Event> found = eventService.findByEndTime(TOURNAMENT_END);

        verify(eventDao).findByEndTime(TOURNAMENT_END);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournament);
    }

    @Test
    public void testFindByEndTimeEmpty(){
        when(eventDao.findByStartTime(OTHER_END)).thenReturn(Collections.emptyList());

        List<Event> found = eventService.findByEndTime(OTHER_END);

        verify(eventDao).findByEndTime(OTHER_END);
        assertThat(found).isEmpty();
    }
}
