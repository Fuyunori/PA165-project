package tennisclub.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dao.CourtDao;
import tennisclub.dao.EventDao;
import tennisclub.entity.Court;
import tennisclub.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CourtServiceTest {
    @MockBean
    private CourtDao courtDao;

    @MockBean
    private EventDao eventDao;

    @Autowired
    private CourtService courtService;

    @Test
    void create() {
        Court court = new Court();
        courtService.create(court);

        verify(courtDao).create(court);
    }

    @Test
    void listByAddress() {
        Court court = new Court();
        when(courtDao.findByAddress("Botanická")).thenReturn(asList(court));

        List<Court> courts = courtService.listByAddress("Botanická");
        assertThat(courts).hasSize(1);
        assertThat(courts).contains(court);
    }

    @Test
    void isFree() {
        LocalDateTime eventStart = LocalDateTime.parse("2021-04-30T16:00");
        LocalDateTime eventEnd = LocalDateTime.parse("2021-04-30T17:00");
        LocalDateTime queryFrom = LocalDateTime.parse("2021-04-30T15:00");
        LocalDateTime queryTo = LocalDateTime.parse("2021-04-30T18:00");

        Court queriedCourt = new Court();
        Court otherCourt = new Court();

        Event eventAtQueried = new Event(eventStart, eventEnd);
        eventAtQueried.setCourt(queriedCourt);
        Event eventElsewhere = new Event(eventStart, eventEnd);
        eventElsewhere.setCourt(otherCourt);

        when(eventDao.findByTimeInterval(queryFrom, queryTo)).thenReturn(List.of(eventAtQueried));
        assertThat(courtService.isFree(queriedCourt, queryFrom, queryTo)).isFalse();

        when(eventDao.findByTimeInterval(queryFrom, queryTo)).thenReturn(List.of(eventElsewhere));
        assertThat(courtService.isFree(queriedCourt, queryFrom, queryTo)).isTrue();

        when(eventDao.findByTimeInterval(queryFrom, queryTo)).thenReturn(List.of(eventAtQueried, eventElsewhere));
        assertThat(courtService.isFree(queriedCourt, queryFrom, queryTo)).isFalse();

        verify(eventDao, times(3)).findByTimeInterval(queryFrom, queryTo);
    }
}
