package tennisclub.facade;

import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisclub.dto.court.CourtDto;
import tennisclub.dto.court.CourtUpdateDto;
import tennisclub.dto.event.EventDTO;
import tennisclub.entity.Court;
import tennisclub.enums.CourtType;
import tennisclub.service.CourtService;
import tennisclub.dto.court.CourtCreateDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pavel Tobiáš
 */
@Service
@Transactional
public class CourtFacadeImpl implements CourtFacade {
    private final Mapper mapper;
    private final CourtService courtService;

    @Autowired
    public CourtFacadeImpl(Mapper mapper, CourtService courtService) {
        this.mapper = mapper;
        this.courtService = courtService;
    }

    @Override
    public CourtDto create(CourtCreateDto court) {
        Court entity = mapper.map(court, Court.class);
        courtService.create(entity);
        return mapper.map(entity, CourtDto.class);
    }

    @Override
    public CourtDto update(Long id, CourtUpdateDto updateDto) {
        Court entity = mapper.map(updateDto, Court.class);
        entity.setId(id);

        Court updatedEntity = courtService.update(entity);
        return mapper.map(updatedEntity, CourtDto.class);
    }

    @Override
    public void delete(Long id) {
        Court court = courtService.getById(id);
        courtService.delete(court);
    }

    @Override
    public CourtDto getById(Long id) {
        Court entity = courtService.getById(id);
        return mapper.map(entity, CourtDto.class);
    }

    @Override
    public List<CourtDto> listAll() {
        List<Court> entities = courtService.listAll();
        return entities.stream()
                .map(e -> mapper.map(e, CourtDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourtDto> listByAddress(String address) {
        List<Court> entities = courtService.listByAddress(address);
        return entities.stream()
                .map(e -> mapper.map(e, CourtDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourtDto> listByType(CourtType type) {
        List<Court> entities = courtService.listByType(type);
        return entities.stream()
                .map(e -> mapper.map(e, CourtDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> listCourtEvents(Long courtId) {
        Court court = courtService.getById(courtId);
        return court.getEvents().stream()
                .map(e -> mapper.map(e, EventDTO.class))
                .collect(Collectors.toList());
    }
}
