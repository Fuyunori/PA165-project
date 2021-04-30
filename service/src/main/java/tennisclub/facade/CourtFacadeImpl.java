package tennisclub.facade;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisclub.dto.court.CourtDto;
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
    public CourtDto update(CourtDto court) {
        Court entity = mapper.map(court, Court.class);
        Court updatedEntity = courtService.update(entity);
        return mapper.map(updatedEntity, CourtDto.class);
    }

    @Override
    public void delete(CourtDto court) {
        Court entity = mapper.map(court, Court.class);
        courtService.delete(entity);
    }

    @Override
    public CourtDto getById(Long id) {
        Court entity = courtService.getById(id);
        return mapper.map(entity, CourtDto.class);
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
}
