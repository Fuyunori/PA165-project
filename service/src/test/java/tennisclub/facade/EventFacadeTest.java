package tennisclub.facade;

import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dto.event.EventRescheduleDTO;
import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.entity.*;
import tennisclub.enums.Level;
import tennisclub.service.CourtService;
import tennisclub.service.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * @author Xuan Linh Phamová
 */
@SpringBootTest
public class EventFacadeTest {
    @MockBean
    private EventService eventService;

    @MockBean
    private CourtService courtService;

    @Autowired
    private EventFacade eventFacade;

    @MockBean
    private Mapper mapper;

    private Event booking;
    private Event lesson;
    private Event tournament;

    private EventWithCourtDTO bookingDTO;
    private EventWithCourtDTO lessonDTO;
    private EventWithCourtDTO tournamentDTO;

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

        bookingDTO = new EventWithCourtDTO();
        bookingDTO.setId(BOOKING_ID);
        bookingDTO.setStartTime(BOOKING_START);
        bookingDTO.setEndTime(BOOKING_END);

        lessonDTO = new EventWithCourtDTO();
        lessonDTO.setId(LESSON_ID);
        lessonDTO.setStartTime(LESSON_START);
        lessonDTO.setEndTime(LESSON_END);

        tournamentDTO = new EventWithCourtDTO();
        tournamentDTO.setId(TOURNAMENT_ID);
        tournamentDTO.setStartTime(TOURNAMENT_START);
        tournamentDTO.setEndTime(TOURNAMENT_END);

    }

    @Test
    public void testRescheduleCourtIsNotFreeBooking(){
        EventRescheduleDTO event = new EventRescheduleDTO(BOOKING_START, BOOKING_END);
        event.setStart(OTHER_START);
        event.setEnd(OTHER_END);

        when(eventService.findById(BOOKING_ID)).thenReturn(booking);
        when(courtService.isFree(booking.getCourt(), OTHER_START, OTHER_END)).thenReturn(false);

        verify(eventService, never()).reschedule(booking, OTHER_START, OTHER_END);
        assertThatThrownBy(() -> eventFacade.reschedule(BOOKING_ID, event));
    }

    @Test
    public void testRescheduleCourtIsNotFreeLesson(){
        EventRescheduleDTO event = new EventRescheduleDTO(LESSON_START, LESSON_END);
        event.setStart(OTHER_START);
        event.setEnd(OTHER_END);

        when(eventService.findById(LESSON_ID)).thenReturn(lesson);
        when(courtService.isFree(lesson.getCourt(), OTHER_START, OTHER_END)).thenReturn(false);

        verify(eventService, never()).reschedule(lesson, OTHER_START, OTHER_END);
        assertThatThrownBy(() -> eventFacade.reschedule(LESSON_ID, event));
    }

    @Test
    public void testRescheduleCourtIsNotFreeTournament(){
        EventRescheduleDTO event = new EventRescheduleDTO(TOURNAMENT_START, TOURNAMENT_END);
        event.setStart(OTHER_START);
        event.setEnd(OTHER_END);

        when(eventService.findById(TOURNAMENT_ID)).thenReturn(tournament);
        when(courtService.isFree(tournament.getCourt(), OTHER_START, OTHER_END)).thenReturn(false);

        verify(eventService, never()).reschedule(tournament, OTHER_START, OTHER_END);
        assertThatThrownBy(() -> eventFacade.reschedule(TOURNAMENT_ID, event));
    }

    @Test
    public void testRescheduleCourtIsFreeBooking(){
        EventRescheduleDTO event = new EventRescheduleDTO(BOOKING_START, BOOKING_END);
        event.setStart(OTHER_START);
        event.setEnd(OTHER_END);

        Event expected = booking;
        expected.setStartTime(OTHER_START);
        expected.setStartTime(OTHER_END);

        EventWithCourtDTO expectedDTO = bookingDTO;
        expectedDTO.setStartTime(OTHER_START);
        expectedDTO.setStartTime(OTHER_END);

        when(eventService.findById(BOOKING_ID)).thenReturn(booking);
        when(eventService.reschedule(booking, OTHER_START, OTHER_END)).thenReturn(expected);
        //when(mapper.map(expected, EventWithCourtDTO.class)).thenReturn(expectedDTO);
        when(courtService.isFree(booking.getCourt(), OTHER_START, OTHER_END)).thenReturn(true);

        EventWithCourtDTO actualDTO = eventFacade.reschedule(BOOKING_ID, event);

        verify(eventService).reschedule(booking, OTHER_START, OTHER_END);
        assertThat(actualDTO).isEqualTo(expectedDTO);
    }

    @Test
    public void testRescheduleCourtIsFreeLesson(){
        EventRescheduleDTO event = new EventRescheduleDTO(LESSON_START, LESSON_END);
        event.setStart(OTHER_START);
        event.setEnd(OTHER_END);

        Event expected = lesson;
        expected.setStartTime(OTHER_START);
        expected.setStartTime(OTHER_END);

        EventWithCourtDTO expectedDTO = lessonDTO;
        expectedDTO.setStartTime(OTHER_START);
        expectedDTO.setStartTime(OTHER_END);

        when(eventService.findById(LESSON_ID)).thenReturn(lesson);
        when(eventService.reschedule(lesson, OTHER_START, OTHER_END)).thenReturn(expected);
        //when(mapper.map(expected, EventWithCourtDTO.class)).thenReturn(expectedDTO);
        when(courtService.isFree(lesson.getCourt(), OTHER_START, OTHER_END)).thenReturn(true);

        EventWithCourtDTO actualDTO = eventFacade.reschedule(LESSON_ID, event);

        verify(eventService).reschedule(lesson, OTHER_START, OTHER_END);
        assertThat(actualDTO).isEqualTo(expectedDTO);
    }

    @Test
    public void testRescheduleCourtIsFreeTournament(){
        EventRescheduleDTO event = new EventRescheduleDTO(TOURNAMENT_START, TOURNAMENT_END);
        event.setStart(OTHER_START);
        event.setEnd(OTHER_END);

        Event expected = tournament;
        expected.setStartTime(OTHER_START);
        expected.setStartTime(OTHER_END);

        EventWithCourtDTO expectedDTO = tournamentDTO;
        expectedDTO.setStartTime(OTHER_START);
        expectedDTO.setStartTime(OTHER_END);

        when(eventService.findById(TOURNAMENT_ID)).thenReturn(tournament);
        when(eventService.reschedule(tournament, OTHER_START, OTHER_END)).thenReturn(expected);
        //when(mapper.map(expected, EventWithCourtDTO.class)).thenReturn(expectedDTO);
        when(courtService.isFree(tournament.getCourt(), OTHER_START, OTHER_END)).thenReturn(true);

        EventWithCourtDTO actualDTO =eventFacade.reschedule(TOURNAMENT_ID,event);

        verify(eventService).reschedule(tournament, OTHER_START, OTHER_END);
        assertThat(actualDTO).isEqualTo(expectedDTO);
    }

    @Test
    public void testFindByIdBooking(){
        when(eventService.findById(BOOKING_ID)).thenReturn(booking);

        EventWithCourtDTO found = eventFacade.findById(BOOKING_ID);
        EventWithCourtDTO expected = mapper.map(booking, EventWithCourtDTO.class);

        verify(eventService).findById(BOOKING_ID);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByIdLesson(){
        when(eventService.findById(LESSON_ID)).thenReturn(lesson);

        EventWithCourtDTO found = eventFacade.findById(LESSON_ID);
        EventWithCourtDTO expected = mapper.map(lesson, EventWithCourtDTO.class);

        verify(eventService).findById(LESSON_ID);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByIdTournament(){
        when(eventService.findById(TOURNAMENT_ID)).thenReturn(tournament);

        EventWithCourtDTO found = eventFacade.findById(TOURNAMENT_ID);
        EventWithCourtDTO expected = mapper.map(tournament, EventWithCourtDTO.class);

        verify(eventService).findById(TOURNAMENT_ID);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindAll(){
        EventWithCourtDTO expectedBooking = mapper.map(booking, EventWithCourtDTO.class);
        EventWithCourtDTO expectedLesson = mapper.map(lesson, EventWithCourtDTO.class);
        EventWithCourtDTO expectedTournament = mapper.map(tournament, EventWithCourtDTO.class);

        when(eventService.findAll()).thenReturn(asList(booking, lesson, tournament));

        List<EventWithCourtDTO> expected = asList(expectedBooking, expectedLesson, expectedTournament);
        List<EventWithCourtDTO> found = eventFacade.findAll();

        verify(eventService).findAll();
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByIntervalAll(){
        EventWithCourtDTO expectedBooking = mapper.map(booking, EventWithCourtDTO.class);
        EventWithCourtDTO expectedLesson = mapper.map(lesson, EventWithCourtDTO.class);
        EventWithCourtDTO expectedTournament = mapper.map(tournament, EventWithCourtDTO.class);

        when(eventService.findByTimeInterval(START, END)).thenReturn(asList(booking, lesson, tournament));

        List<EventWithCourtDTO> expected = asList(expectedBooking, expectedLesson, expectedTournament);
        List<EventWithCourtDTO> found = eventFacade.findByTimeInterval(START, END);

        verify(eventService).findByTimeInterval(START,END);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByIntervalBooking(){
        EventWithCourtDTO expectedBooking = mapper.map(booking, EventWithCourtDTO.class);

        when(eventService.findByTimeInterval(BOOKING_START, BOOKING_END)).thenReturn(Collections.singletonList(booking));

        List<EventWithCourtDTO> expected = Collections.singletonList(expectedBooking);
        List<EventWithCourtDTO> found = eventFacade.findByTimeInterval(BOOKING_START, BOOKING_END);

        verify(eventService).findByTimeInterval(BOOKING_START,BOOKING_END);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByIntervalLesson(){
        EventWithCourtDTO expectedLesson = mapper.map(lesson, EventWithCourtDTO.class);

        when(eventService.findByTimeInterval(LESSON_START, LESSON_END)).thenReturn(Collections.singletonList(lesson));

        List<EventWithCourtDTO> expected = Collections.singletonList(expectedLesson);
        List<EventWithCourtDTO> found = eventFacade.findByTimeInterval(LESSON_START, LESSON_END);

        verify(eventService).findByTimeInterval(LESSON_START,LESSON_END);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByIntervalTournament(){
        EventWithCourtDTO expectedTournament = mapper.map(tournament, EventWithCourtDTO.class);

        when(eventService.findByTimeInterval(TOURNAMENT_START, TOURNAMENT_END)).thenReturn(Collections.singletonList(tournament));

        List<EventWithCourtDTO> expected = Collections.singletonList(expectedTournament);
        List<EventWithCourtDTO> found = eventFacade.findByTimeInterval(TOURNAMENT_START, TOURNAMENT_END);

        verify(eventService).findByTimeInterval(TOURNAMENT_START,TOURNAMENT_END);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByIntervalEmpty(){
        when(eventService.findByTimeInterval(OTHER_START, OTHER_END)).thenReturn(Collections.emptyList());

        List<EventWithCourtDTO> expected = Collections.emptyList();
        List<EventWithCourtDTO> found = eventFacade.findByTimeInterval(OTHER_START, OTHER_END);

        verify(eventService).findByTimeInterval(OTHER_START,OTHER_END);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByStartTimeBooking(){
        EventWithCourtDTO expectedBooking = mapper.map(booking, EventWithCourtDTO.class);

        when(eventService.findByStartTime(BOOKING_START)).thenReturn(Collections.singletonList(booking));

        List<EventWithCourtDTO> expected = Collections.singletonList(expectedBooking);
        List<EventWithCourtDTO> found = eventFacade.findByStartTime(BOOKING_START);

        verify(eventService).findByStartTime(BOOKING_START);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByStartTimeLesson(){
        EventWithCourtDTO expectedLesson = mapper.map(lesson, EventWithCourtDTO.class);

        when(eventService.findByStartTime(LESSON_START)).thenReturn(Collections.singletonList(lesson));

        List<EventWithCourtDTO> expected = Collections.singletonList(expectedLesson);
        List<EventWithCourtDTO> found = eventFacade.findByStartTime(LESSON_START);

        verify(eventService).findByStartTime(LESSON_START);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByStartTimeTournament(){
        EventWithCourtDTO expectedTournament = mapper.map(tournament, EventWithCourtDTO.class);

        when(eventService.findByStartTime(TOURNAMENT_START)).thenReturn(Collections.singletonList(tournament));

        List<EventWithCourtDTO> expected = Collections.singletonList(expectedTournament);
        List<EventWithCourtDTO> found = eventFacade.findByStartTime(TOURNAMENT_START);

        verify(eventService).findByStartTime(TOURNAMENT_START);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByStartTimeEmpty(){
        when(eventService.findByStartTime(OTHER_START)).thenReturn(Collections.emptyList());

        List<EventWithCourtDTO> expected = Collections.emptyList();
        List<EventWithCourtDTO> found = eventFacade.findByStartTime(OTHER_START);

        verify(eventService).findByStartTime(OTHER_START);
        assertThat(found).isEqualTo(expected);
    }


    @Test
    public void testFindByEndTimeBooking(){
        EventWithCourtDTO expectedBooking = mapper.map(booking, EventWithCourtDTO.class);

        when(eventService.findByEndTime(BOOKING_END)).thenReturn(Collections.singletonList(booking));

        List<EventWithCourtDTO> expected = Collections.singletonList(expectedBooking);
        List<EventWithCourtDTO> found = eventFacade.findByEndTime(BOOKING_END);

        verify(eventService).findByEndTime(BOOKING_END);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByEndTimeLesson(){
        EventWithCourtDTO expectedLesson = mapper.map(lesson, EventWithCourtDTO.class);

        when(eventService.findByEndTime(LESSON_END)).thenReturn(Collections.singletonList(lesson));

        List<EventWithCourtDTO> expected = Collections.singletonList(expectedLesson);
        List<EventWithCourtDTO> found = eventFacade.findByEndTime(LESSON_END);

        verify(eventService).findByEndTime(LESSON_END);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByEndTimeTournament(){
        EventWithCourtDTO expectedTournament = mapper.map(tournament, EventWithCourtDTO.class);

        when(eventService.findByEndTime(TOURNAMENT_END)).thenReturn(Collections.singletonList(tournament));

        List<EventWithCourtDTO> expected = Collections.singletonList(expectedTournament);
        List<EventWithCourtDTO> found = eventFacade.findByEndTime(TOURNAMENT_END);

        verify(eventService).findByEndTime(TOURNAMENT_END);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByEndTimeEmpty(){
        when(eventService.findByEndTime(OTHER_END)).thenReturn(Collections.emptyList());

        List<EventWithCourtDTO> expected = Collections.emptyList();
        List<EventWithCourtDTO> found = eventFacade.findByEndTime(OTHER_END);

        verify(eventService).findByEndTime(OTHER_END);
        assertThat(found).isEqualTo(expected);
    }
}
