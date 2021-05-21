package tennisclub.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import tennisclub.ServiceTestsConfiguration;
import tennisclub.dto.booking.BookingFullDTO;
import tennisclub.dto.court.CourtCreateDto;
import tennisclub.dto.court.CourtDto;
import tennisclub.dto.court.CourtUpdateDto;
import tennisclub.dto.event.EventDTO;
import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.entity.Booking;
import tennisclub.entity.Court;
import tennisclub.entity.Event;
import tennisclub.enums.CourtType;
import tennisclub.service.CourtService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;

/**
 * @author Ondrej Holub
 */
@SpringBootTest
@ContextConfiguration(classes = ServiceTestsConfiguration.class)
public class CourtFacadeTest {
    @MockBean
    private CourtService courtService;

    @Autowired
    private CourtFacade courtFacade;

    @Test
    void create() {
        CourtCreateDto passedDto = new CourtCreateDto();
        passedDto.setName("Hello");
        passedDto.setPreviewImageUrl("http://localhost/image.png");
        passedDto.setType(CourtType.TURF);
        passedDto.setAddress("Abbey Road");
        courtFacade.create(passedDto);

        Court expectedEntity = new Court();
        expectedEntity.setName("Hello");
        expectedEntity.setPreviewImageUrl("http://localhost/image.png");
        expectedEntity.setType(CourtType.TURF);
        expectedEntity.setAddress("Abbey Road");
        verify(courtService).create(refEq(expectedEntity));
    }

    @Test
    void update() {
        Court expectedEntity = createSampleCourtEntity();
        CourtDto expectedDto = createSampleCourtDto();

        CourtUpdateDto passedDto = createSampleCourtUpdateDto();
        Long passedId = expectedEntity.getId();

        when(courtService.update(expectedEntity)).thenReturn(expectedEntity);

        CourtDto result = courtFacade.update(passedId, passedDto);
        verify(courtService).update(refEq(expectedEntity));
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void delete() {
        Long passedId = 42L;
        Court deletedEntity = createSampleCourtEntity();

        when(courtService.getById(passedId)).thenReturn(deletedEntity);

        courtFacade.delete(passedId);
        verify(courtService).getById(passedId);
        verify(courtService).delete(deletedEntity);
    }

    @Test
    void getById() {
        Court returnedEntity = createSampleCourtEntity();
        CourtDto expectedDTO = createSampleCourtDto();

        when(courtService.getById(42L)).thenReturn(returnedEntity);
        assertThat(courtFacade.getById(42L)).isEqualTo(expectedDTO);
        verify(courtService).getById(42L);
    }

    @Test
    void listByAddress() {
        Court entity1 = createSampleCourtEntity();

        Court entity2 = new Court();
        entity2.setId(360L);
        entity2.setName("Noscope");
        entity2.setPreviewImageUrl("http://globalhost/image.bmp");
        entity2.setType(CourtType.GRASS);
        entity2.setAddress("Abbey Road");

        List<Court> list = asList(entity1, entity2);
        when(courtService.listByAddress("Abbey Road")).thenReturn(list);

        CourtDto expected1 = createSampleCourtDto();

        CourtDto expected2 = new CourtDto();
        expected2.setId(360L);
        expected2.setName("Noscope");
        expected2.setPreviewImageUrl("http://globalhost/image.bmp");
        expected2.setType(CourtType.GRASS);
        expected2.setAddress("Abbey Road");

        List<CourtDto> obtained = courtFacade.listByAddress("Abbey Road");
        verify(courtService).listByAddress("Abbey Road");

        assertThat(obtained).contains(expected1, expected2);
        assertThat(obtained).hasSize(2);
    }

    @Test
    void listByType() {
        Court entity1 = createSampleCourtEntity();

        Court entity2 = new Court();
        entity2.setId(360L);
        entity2.setName("Noscope");
        entity2.setPreviewImageUrl("http://globalhost/image.bmp");
        entity2.setType(CourtType.TURF);
        entity2.setAddress("Boboddy Road");

        List<Court> list = asList(entity1, entity2);
        when(courtService.listByType(CourtType.TURF)).thenReturn(list);

        CourtDto expected1 = createSampleCourtDto();

        CourtDto expected2 = new CourtDto();
        expected2.setId(360L);
        expected2.setName("Noscope");
        expected2.setPreviewImageUrl("http://globalhost/image.bmp");
        expected2.setType(CourtType.TURF);
        expected2.setAddress("Boboddy Road");

        List<CourtDto> obtained = courtFacade.listByType(CourtType.TURF);
        verify(courtService).listByType(CourtType.TURF);

        assertThat(obtained).contains(expected1, expected2);
        assertThat(obtained).hasSize(2);
    }

    @Test
    void listCourtEvents() {
        LocalDateTime startTime1 = LocalDateTime.parse("2021-04-30T16:00");
        LocalDateTime endTime1 = LocalDateTime.parse("2021-04-30T17:00");
        Event event1 = new Booking(startTime1, endTime1);
        BookingFullDTO eventDto1 = new BookingFullDTO();
        eventDto1.setStartTime(startTime1);
        eventDto1.setEndTime(endTime1);

        LocalDateTime startTime2 = LocalDateTime.parse("2021-05-01T12:00");
        LocalDateTime endTime2 = LocalDateTime.parse("2021-05-01T14:00");
        Event event2 = new Booking(startTime2, endTime2);
        BookingFullDTO eventDto2 = new BookingFullDTO();
        eventDto2.setStartTime(startTime2);
        eventDto2.setEndTime(endTime2);

        Court court = mock(Court.class);
        when(court.getEvents()).thenReturn(Set.of(event1, event2));
        when(courtService.getById(42L)).thenReturn(court);

        assertThat(courtFacade.listCourtEvents(42L)).hasSize(2).contains(eventDto1, eventDto2);
        verify(courtService).getById(42L);
        verify(court).getEvents();
    }

    private CourtDto createSampleCourtDto() {
        CourtDto sample = new CourtDto();
        sample.setId(42L);
        sample.setName("Hello");
        sample.setPreviewImageUrl("http://localhost/image.png");
        sample.setType(CourtType.TURF);
        sample.setAddress("Abbey Road");
        return sample;
    }

    private CourtUpdateDto createSampleCourtUpdateDto() {
        CourtUpdateDto sample = new CourtUpdateDto();
        sample.setName("Hello");
        sample.setPreviewImageUrl("http://localhost/image.png");
        sample.setType(CourtType.TURF);
        sample.setAddress("Abbey Road");
        return sample;
    }

    private Court createSampleCourtEntity() {
        Court sample = new Court();
        sample.setId(42L);
        sample.setName("Hello");
        sample.setPreviewImageUrl("http://localhost/image.png");
        sample.setType(CourtType.TURF);
        sample.setAddress("Abbey Road");
        return sample;
    }

}
