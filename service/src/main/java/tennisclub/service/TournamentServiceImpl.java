package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.RankingDao;
import tennisclub.dao.TournamentDao;
import tennisclub.entity.Court;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;
import tennisclub.entity.ranking.Ranking;
import tennisclub.exceptions.ServiceLayerException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TournamentServiceImpl implements TournamentService {
    private final RankingDao rankingDao;
    private final TournamentDao tournamentDao;
    private final TimeService timeService;

    @Autowired
    public TournamentServiceImpl(TournamentDao tournamentDao, RankingDao rankingDao, TimeService timeService){
        this.tournamentDao = tournamentDao;
        this.rankingDao = rankingDao;
        this.timeService = timeService;
    }

    @Override
    public Tournament create(Tournament tournament) {
        tournamentDao.create(tournament);
        return tournament;
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
    public Tournament findById(Long id){
        return tournamentDao.findById(id);
    }

    @Override
    public List<Tournament> findAll(){
        return tournamentDao.findAll();
    }

    @Override
    public List<Tournament> findByCourt(Court court) {
        return tournamentDao.findByCourt(court);
    }

    @Override
    public List<Tournament> findByStartTime(LocalDateTime startTime) {
        return tournamentDao.findByStartTime(startTime);
    }

    @Override
    public List<Tournament> findByEndTime(LocalDateTime endTime) {
        return tournamentDao.findByEndTime(endTime);
    }

    @Override
    public List<Tournament> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return tournamentDao.findByTimeInterval(from, to);
    }

    @Override
    public List<Tournament> findByCapacity(Integer capacity) {
        return tournamentDao.findByCapacity(capacity);
    }

    @Override
    public List<Ranking> findRanking(Tournament tournament, User player) {
        return rankingDao.findByTournament(tournament);
    }

    @Override
    public List<Ranking> findRankingByTournament(Tournament tournament) {
        return rankingDao.findByTournament(tournament);
    }

    @Override
    public List<Ranking> findRankingByPlayer(User player) {
        return rankingDao.findByUser(player);
    }

    @Override
    public void enrollPlayer(Tournament tournament, User player) {
        if(rankingDao.find(tournament, player) != null){
            throw new ServiceLayerException("Can't enroll a player into a tournament in which he/she already participates!");
        }

        checkEnrollmentOpen(tournament);

        Ranking ranking = new Ranking(tournament, player);
        rankingDao.create(ranking);
    }

    @Override
    public void withdrawPlayer(Tournament tournament, User player) {
        if(rankingDao.find(tournament, player) == null){
            throw new ServiceLayerException("Can't withdraw a player from a tournament in which he/she doesn't participate!");
        }

        checkEnrollmentOpen(tournament);

        Ranking ranking = rankingDao.find(tournament, player);
        rankingDao.delete(ranking);
    }

    @Override
    public Ranking rankPlayer(Ranking ranking, Integer newPlacement) {
        checkEnrollmentOpen(ranking.getTournament());

        ranking.setPlayerPlacement(newPlacement);
        return rankingDao.update(ranking);
    }

    private void checkEnrollmentOpen(Tournament tournament) {
        final LocalDateTime CURRENT_DATE_TIME = timeService.getCurrentDateTime();
        if (CURRENT_DATE_TIME.isAfter(tournament.getStartTime())) {
            throw new ServiceLayerException("Can't enroll/withdraw user from a tournament has already started!");
        }
    }
}
