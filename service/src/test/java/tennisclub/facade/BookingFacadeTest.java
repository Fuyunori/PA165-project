package tennisclub.facade;

import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dto.booking.BookingCreateDTO;
import tennisclub.dto.booking.BookingFullDTO;
import tennisclub.dto.court.CourtDto;
import tennisclub.entity.*;
import tennisclub.exceptions.FacadeLayerException;
import tennisclub.exceptions.ServiceLayerException;
import tennisclub.service.BookingService;
import tennisclub.service.CourtService;
import tennisclub.service.UserService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * @author Xuan Linh Phamová
 */
@SpringBootTest
public class BookingFacadeTest {
    @MockBean
    private BookingService bookingService;

    @MockBean
    private CourtService courtService;

    @MockBean
    private UserService userService;

    @Autowired
    private BookingFacade bookingFacade;

    @MockBean
    private Mapper mapper;

    private Booking booking;

    private final LocalDate START_DAY = LocalDate.of(2048, 4,1);

    private final LocalTime START_TIME = LocalTime.MIN;

    // booking info
    private final Long BOOKING_ID = 1L;
    private final LocalDateTime BOOKING_START = LocalDateTime.of(START_DAY, START_TIME);;
    private final LocalDateTime BOOKING_END = BOOKING_START.plusHours(2);

    private final LocalDateTime OTHER_START = BOOKING_START.plusDays(100);
    private final LocalDateTime OTHER_END = BOOKING_END.plusDays(100);

    private final Court court = new Court("Court 1");
    private final Court otherCourt = new Court("Court 2");

    private final Long COURT_ID = 1L;
    private CourtDto courtDto = new CourtDto();

    private final Long PARTICIPANT_ID = 1L;
    private User participant = new User();

    private BookingFullDTO bookingFullDTO;

    @BeforeEach
    public void setup(){
        booking = new Booking(BOOKING_START, BOOKING_END);
        booking.setId(BOOKING_ID);
        booking.setCourt(court);

        courtDto.setId(COURT_ID);
        courtDto.setName(court.getName());

        participant.setUsername("unavený student pa165");
        participant.setId(PARTICIPANT_ID);

        bookingFullDTO = new BookingFullDTO();
        bookingFullDTO.setId(BOOKING_ID);
        bookingFullDTO.setStartTime(BOOKING_START);
        bookingFullDTO.setEndTime(BOOKING_END);
        bookingFullDTO.setCourt(courtDto);
    }


    @Test
    public void testMakeBookingCourtIsNotFree(){
        BookingCreateDTO bookingCreateDTO = new BookingCreateDTO();
        bookingCreateDTO.setStartTime(BOOKING_START);
        bookingCreateDTO.setEndTime(BOOKING_END);
        bookingCreateDTO.setCourt(courtDto);

        when(mapper.map(bookingCreateDTO, Booking.class)).thenReturn(booking);
        when(courtService.isFree(court, BOOKING_START, BOOKING_END)).thenReturn(false);

        verify(bookingService, never()).create(any(Booking.class));
        assertThatThrownBy(()-> bookingFacade.makeBooking(bookingCreateDTO)).isInstanceOf(FacadeLayerException.class);
    }

    @Test
    public void testMakeBookingCourtReserveMoreThan2Hours(){
        when(courtService.isFree(court, BOOKING_START, BOOKING_END.plusHours(1))).thenReturn(true);
        when(bookingService.getTotalReservedHoursToday(any(User.class))).thenReturn(Duration.ofHours(3));

        BookingCreateDTO bookingCreateDTO = new BookingCreateDTO();
        bookingCreateDTO.setStartTime(BOOKING_START);
        bookingCreateDTO.setEndTime(BOOKING_END.plusHours(1));
        bookingCreateDTO.setCourt(courtDto);

        Booking mappedBooking = booking;
        mappedBooking.setEndTime(BOOKING_END.plusHours(1));

        when(mapper.map(bookingCreateDTO, Booking.class)).thenReturn(mappedBooking);

        verify(bookingService, never()).create(any(Booking.class));
        assertThatThrownBy(()-> bookingFacade.makeBooking(bookingCreateDTO)).isInstanceOf(FacadeLayerException.class);
    }

    @Test
    public void testMakeBookingCourtOK(){
        when(courtService.isFree(court, BOOKING_START, BOOKING_END)).thenReturn(true);
        when(bookingService.getTotalReservedHoursToday(any(User.class))).thenReturn(Duration.ofHours(3));
        when(bookingService.create(booking)).thenReturn(booking);

        BookingCreateDTO bookingCreateDTO = new BookingCreateDTO();
        bookingCreateDTO.setStartTime(BOOKING_START);
        bookingCreateDTO.setEndTime(BOOKING_END);
        bookingCreateDTO.setCourt(courtDto);

        when(mapper.map(bookingCreateDTO, Booking.class)).thenReturn(booking);

        BookingFullDTO bookingDTO = bookingFacade.makeBooking(bookingCreateDTO);

        verify(bookingService).create(booking);
        assertThat(bookingDTO).isEqualTo(bookingFullDTO);
    }

    @Test
    public void testCancelBooking(){
        when(bookingService.findById(BOOKING_ID)).thenReturn(booking);

        bookingFacade.cancelBooking(BOOKING_ID);

        verify(bookingService).remove(booking);
    }

    @Test
    public void testAddUser(){
        when(bookingService.findById(BOOKING_ID)).thenReturn(booking);
        when(userService.findUserById(PARTICIPANT_ID)).thenReturn(participant);

        bookingFacade.addUser(BOOKING_ID, PARTICIPANT_ID);

        verify(bookingService).addUser(booking, participant);

    }

    @Test
    public void testAddUserButServiceFails(){
        when(bookingService.findById(BOOKING_ID)).thenReturn(booking);
        when(userService.findUserById(PARTICIPANT_ID)).thenReturn(participant);

        when(bookingService.addUser(booking, participant)).thenThrow(new ServiceLayerException());

        verify(bookingService, never()).addUser(booking, participant);
        assertThatThrownBy(() -> bookingFacade.addUser(BOOKING_ID, PARTICIPANT_ID)).isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void testRemoveUser(){
        when(bookingService.findById(BOOKING_ID)).thenReturn(booking);
        when(userService.findUserById(PARTICIPANT_ID)).thenReturn(participant);

        bookingFacade.removeUser(BOOKING_ID, PARTICIPANT_ID);

        verify(bookingService).removeUser(booking, participant);

    }

    @Test
    public void testRemoveUserButServiceFails(){
        when(bookingService.findById(BOOKING_ID)).thenReturn(booking);
        when(userService.findUserById(PARTICIPANT_ID)).thenReturn(participant);

        when(bookingService.removeUser(booking, participant)).thenThrow(new ServiceLayerException());

        verify(bookingService, never()).addUser(booking, participant);
        assertThatThrownBy(() -> bookingFacade.removeUser(BOOKING_ID, PARTICIPANT_ID)).isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void testFindByIdBooking(){
        when(bookingService.findById(BOOKING_ID)).thenReturn(booking);

        BookingFullDTO found = bookingFacade.findById(BOOKING_ID);
        BookingFullDTO expected = mapper.map(booking, BookingFullDTO.class);

        verify(bookingService).findById(BOOKING_ID);
        assertThat(found).isEqualTo(expected);
    }


    @Test
    public void testFindAll(){
        BookingFullDTO expectedBooking = mapper.map(booking, BookingFullDTO.class);
        when(bookingService.findAll()).thenReturn(Collections.singletonList(booking));

        List<BookingFullDTO> expected = Collections.singletonList(expectedBooking);
        List<BookingFullDTO> found = bookingFacade.findAll();

        verify(bookingService).findAll();
        assertThat(found).isEqualTo(expected);
    }


    @Test
    public void testFindByIntervalBooking(){
        BookingFullDTO expectedBooking = mapper.map(booking, BookingFullDTO.class);

        when(bookingService.findByTimeInterval(BOOKING_START, BOOKING_END)).thenReturn(Collections.singletonList(booking));

        List<BookingFullDTO> expected = Collections.singletonList(expectedBooking);
        List<BookingFullDTO> found = bookingFacade.findByTimeInterval(BOOKING_START, BOOKING_END);

        verify(bookingService).findByTimeInterval(BOOKING_START,BOOKING_END);
        assertThat(found).isEqualTo(expected);
    }

    @Test
    public void testFindByIntervalEmpty(){
        when(bookingService.findByTimeInterval(OTHER_START, OTHER_END)).thenReturn(Collections.emptyList());

        List<BookingFullDTO> expected = Collections.emptyList();
        List<BookingFullDTO> found = bookingFacade.findByTimeInterval(OTHER_START, OTHER_END);

        verify(bookingService).findByTimeInterval(OTHER_START,OTHER_END);
        assertThat(found).isEqualTo(expected);
    }



}
