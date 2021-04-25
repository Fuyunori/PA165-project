package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.TournamentDao;
import tennisclub.entity.Tournament;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TournamentServiceImpl implements TournamentService {
    private final TournamentDao tournamentDao;

    @Autowired
    public TournamentServiceImpl(TournamentDao tournamentDao){
        this.tournamentDao = tournamentDao;
    }

    @Override
    public void create(Tournament tournament) {
        tournamentDao.create(tournament);
    }

    @Override
    public Tournament update(Tournament tournament) {
        return tournamentDao.update(tournament);
    }

    @Override
    public void remove(Tournament tournament) {
        tournamentDao.remove(tournament);
    }

    @Override
    public List<Tournament> listByStartTime(LocalDateTime startTime) {
        return tournamentDao.findByStartTime(startTime);
    }

    @Override
    public List<Tournament> listByEndTime(LocalDateTime endTime) {
        return tournamentDao.findByEndTime(endTime);
    }
}
