package tennisclub.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dto.court.CourtCreateDto;
import tennisclub.dto.court.CourtDto;
import tennisclub.entity.Court;
import tennisclub.enums.CourtType;
import tennisclub.service.CourtService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
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
        CourtDto passedDto = new CourtDto();
        passedDto.setId(42L);
        passedDto.setName("Hello");
        passedDto.setPreviewImageUrl("http://localhost/image.png");
        passedDto.setType(CourtType.TURF);
        passedDto.setAddress("Abbey Road");

        Court expectedEntity = new Court();
        expectedEntity.setId(42L);
        expectedEntity.setName("Hello");
        expectedEntity.setPreviewImageUrl("http://localhost/image.png");
        expectedEntity.setType(CourtType.TURF);
        expectedEntity.setAddress("Abbey Road");

        when(courtService.update(expectedEntity)).thenReturn(expectedEntity);

        CourtDto result = courtFacade.update(passedDto);
        verify(courtService).update(refEq(expectedEntity));
        assertThat(result).isEqualTo(passedDto);
    }
}
