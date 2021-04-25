package tennisclub.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dto.CourtDto;
import tennisclub.entity.Court;
import tennisclub.enums.CourtType;
import tennisclub.service.CourtService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CourtFacadeTest {
    @MockBean
    private CourtService courtService;

    @Autowired
    private CourtFacade courtFacade;

    @Test
    void create() {
        CourtDto passedDto = new CourtDto();
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
}
