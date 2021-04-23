package tennisclub.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dao.CourtDao;
import tennisclub.entity.Court;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CourtServiceTest {
    @MockBean
    private CourtDao courtDao;

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
}
