package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.CourtDao;
import tennisclub.dao.EventDao;
import tennisclub.entity.Court;
import tennisclub.entity.Event;
import tennisclub.enums.CourtType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CourtService}
 * @author Pavel Tobiáš
 */
@Service
public class CourtServiceImpl implements CourtService {
    private final CourtDao courtDao;
    private final EventDao eventDao;

    @Autowired
    public CourtServiceImpl(CourtDao courtDao, EventDao eventDao) {
        this.courtDao = courtDao;
        this.eventDao = eventDao;
    }

    @Override
    public void create(Court court) {
        courtDao.create(court);
    }

    @Override
    public Court update(Court court) {
        return courtDao.update(court);
    }

    @Override
    public void delete(Court court) {
        courtDao.delete(court);
    }

    @Override
    public Court getById(Long id) {
        return courtDao.findById(id);
    }

    @Override
    public List<Court> listAll() {
        return courtDao.findAll();
    }

    @Override
    public List<Court> listByAddress(String address) {
        return courtDao.findByAddress(address);
    }

    @Override
    public List<Court> listByType(CourtType type) {
        return courtDao.findByType(type);
    }

    @Override
    public boolean isFree(Court court, LocalDateTime from, LocalDateTime to) {
        return eventDao.findByTimeInterval(from, to).stream()
                .noneMatch(event -> court.equals(event.getCourt()));
    }

    @Override
    public List<Event> getConflictingEvents(Court court, LocalDateTime from, LocalDateTime to) {
        return eventDao.findByTimeInterval(from, to).stream()
                .filter(event -> court.equals(event.getCourt()))
                .collect(Collectors.toList());
    }
}
