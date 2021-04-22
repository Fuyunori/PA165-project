package tennisclub.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dao.CourtDao;
import tennisclub.entity.Court;

import static org.mockito.Mockito.verify;

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
}
