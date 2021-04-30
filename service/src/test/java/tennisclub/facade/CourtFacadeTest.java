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

import java.util.List;

import static java.util.Arrays.asList;
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

    @Test
    void delete() {
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

        courtFacade.delete(passedDto);
        verify(courtService).delete(refEq(expectedEntity));
    }

    @Test
    void getById() {
        Court returnedEntity = new Court();
        returnedEntity.setId(42L);
        returnedEntity.setName("Hello");
        returnedEntity.setPreviewImageUrl("http://localhost/image.png");
        returnedEntity.setType(CourtType.TURF);
        returnedEntity.setAddress("Abbey Road");

        CourtDto expectedDTO = new CourtDto();
        expectedDTO.setId(42L);
        expectedDTO.setName("Hello");
        expectedDTO.setPreviewImageUrl("http://localhost/image.png");
        expectedDTO.setType(CourtType.TURF);
        expectedDTO.setAddress("Abbey Road");

        when(courtService.getById(42L)).thenReturn(returnedEntity);
        assertThat(courtFacade.getById(42L)).isEqualTo(expectedDTO);
    }

    @Test
    void listByAddress() {
        Court entity1 = new Court();
        entity1.setId(42L);
        entity1.setName("Hello");
        entity1.setPreviewImageUrl("http://localhost/image.png");
        entity1.setType(CourtType.TURF);
        entity1.setAddress("Abbey Road");

        Court entity2 = new Court();
        entity2.setId(360L);
        entity2.setName("Noscope");
        entity2.setPreviewImageUrl("http://globalhost/image.bmp");
        entity2.setType(CourtType.GRASS);
        entity2.setAddress("Abbey Road");

        List<Court> list = asList(entity1, entity2);
        when(courtService.listByAddress("Abbey Road")).thenReturn(list);

        CourtDto expected1 = new CourtDto();
        expected1.setId(42L);
        expected1.setName("Hello");
        expected1.setPreviewImageUrl("http://localhost/image.png");
        expected1.setType(CourtType.TURF);
        expected1.setAddress("Abbey Road");

        CourtDto expected2 = new CourtDto();
        expected2.setId(360L);
        expected2.setName("Noscope");
        expected2.setPreviewImageUrl("http://globalhost/image.bmp");
        expected2.setType(CourtType.GRASS);
        expected2.setAddress("Abbey Road");

        List<CourtDto> obtained = courtFacade.listByAddress("Abbey Road");

        assertThat(obtained).contains(expected1, expected2);
        assertThat(obtained).hasSize(2);
    }
}
