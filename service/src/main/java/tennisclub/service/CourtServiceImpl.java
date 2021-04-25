package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.CourtDao;
import tennisclub.entity.Court;
import tennisclub.enums.CourtType;
import java.util.List;

/**
 * Implementation of {@link CourtService}
 * @author Pavel Tobiáš
 */
@Service
public class CourtServiceImpl implements CourtService {
    private final CourtDao courtDao;

    @Autowired
    public CourtServiceImpl(CourtDao courtDao) {
        this.courtDao = courtDao;
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
    public List<Court> listByAddress(String address) {
        return courtDao.findByAddress(address);
    }

    @Override
    public List<Court> listByType(CourtType type) {
        return courtDao.findByType(type);
    }
}
