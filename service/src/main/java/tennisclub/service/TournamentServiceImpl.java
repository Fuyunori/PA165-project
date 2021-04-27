package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.TournamentDao;
import tennisclub.entity.Tournament;
import tennisclub.entity.ranking.Ranking;
import tennisclub.exceptions.TennisClubManagerException;

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
    public void addRanking(Tournament tournament, Ranking ranking){
        if(tournament.getRankings().contains(ranking)){
            throw new TennisClubManagerException("Tournament already contains the rank of the player.\n Tournament: "
                    + tournament.getId() + ",\n player: "
                    + ranking.getPlayer().getId());
        }
        tournament.addRanking(ranking);
    }

    @Override
    public Tournament listById(Long id){
        return tournamentDao.findById(id);
    }

    @Override
    public List<Tournament> listAll(){
        return tournamentDao.findAll();
    }

    @Override
    public List<Tournament> listByStartTime(LocalDateTime startTime) {
        return tournamentDao.findByStartTime(startTime);
    }

    @Override
    public List<Tournament> listByEndTime(LocalDateTime endTime) {
        return tournamentDao.findByEndTime(endTime);
    }

    @Override
    public List<Tournament> listByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return tournamentDao.findByTimeInterval(from, to);
    }

    @Override
    public List<Tournament> listByCapacity(Integer capacity) {
        return tournamentDao.findByCapacity(capacity);
    }
}
