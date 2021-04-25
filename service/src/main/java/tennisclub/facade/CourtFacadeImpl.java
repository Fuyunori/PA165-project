package tennisclub.facade;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dto.CourtDto;
import tennisclub.entity.Court;
import tennisclub.enums.CourtType;
import tennisclub.service.CourtService;
import tennisclub.dto.CourtCreateDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pavel Tobiáš
 */
@Service
public class CourtFacadeImpl implements CourtFacade {
    private final Mapper mapper;
    private final CourtService courtService;

    @Autowired
    public CourtFacadeImpl(Mapper mapper, CourtService courtService) {
        this.mapper = mapper;
        this.courtService = courtService;
    }

    @Override
    public void create(CourtCreateDto court) {
        Court entity = mapper.map(court, Court.class);
        courtService.create(entity);
    }

    @Override
    public void update(CourtDto court) {
        Court entity = mapper.map(court, Court.class);
        courtService.update(entity);
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
