package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;
import tennisclub.entity.ranking.Ranking;

import java.util.List;

@Service
public interface RankingService {
    void create(Ranking ranking);
    Ranking find(Tournament tournament, User player);
    List<Ranking> findByPlayer(User player);
    List<Ranking> findByTournament(Tournament tournament);
    Ranking update(Ranking ranking);
    void remove(Ranking ranking);
}
