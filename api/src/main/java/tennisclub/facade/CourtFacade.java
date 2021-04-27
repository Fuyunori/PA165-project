package tennisclub.facade;

import tennisclub.dto.CourtCreateDto;
import tennisclub.dto.CourtDto;
import tennisclub.enums.CourtType;
import java.util.List;

/**
 * @author Pavel Tobiáš
 */
public interface CourtFacade {
    CourtDto create(CourtCreateDto court);
    CourtDto update(CourtDto court);
    void delete(CourtDto court);
    CourtDto getById(Long id);
    List<CourtDto> listByAddress(String address);
    List<CourtDto> listByType(CourtType type);
}