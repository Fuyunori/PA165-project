package tennisclub.service;

import tennisclub.entity.Tournament;
import tennisclub.entity.User;
import tennisclub.entity.ranking.Ranking;

import java.util.List;

public interface RankingService {
    void create(Ranking ranking);
    Ranking find(User player, Tournament tournament);
    List<Ranking> findByPlayer(User player);
    List<Ranking> findByTournament(Tournament tournament);
    void update(Ranking ranking);
    void remove(Ranking ranking);
}
