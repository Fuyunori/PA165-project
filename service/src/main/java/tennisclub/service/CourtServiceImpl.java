package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.CourtDao;
import tennisclub.entity.Court;
import java.util.List;

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
    public List<Court> listByAddress(String address) {
        return courtDao.findByAddress(address);
    }
}
