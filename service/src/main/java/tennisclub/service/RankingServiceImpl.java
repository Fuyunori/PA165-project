package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.RankingDao;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;
import tennisclub.entity.ranking.Ranking;

import java.util.List;

@Service
public class RankingServiceImpl implements RankingService {

    private final RankingDao rankingDao;

    @Autowired
    RankingServiceImpl(RankingDao rankingDao) { this.rankingDao = rankingDao; }

    @Override
    public void create(Ranking ranking) {
        rankingDao.create(ranking);
    }

    @Override
    public Ranking find(Tournament tournament, User player) {
        return rankingDao.find(tournament, player);
    }

    @Override
    public List<Ranking> findByPlayer(User player) {
        return rankingDao.findByUser(player);
    }

    @Override
    public List<Ranking> findByTournament(Tournament tournament) {
        return rankingDao.findByTournament(tournament);
    }

    @Override
    public void update(Ranking ranking) {
        rankingDao.update(ranking);
    }

    @Override
    public void remove(Ranking ranking) {
        rankingDao.delete(ranking);
    }
}
