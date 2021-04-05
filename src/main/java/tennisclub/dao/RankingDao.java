package tennisclub.dao;

import org.springframework.stereotype.Repository;
import tennisclub.entity.ranking.Ranking;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;

import java.util.List;

/**
 * @author Ondrej Holub
 */
@Repository
public interface RankingDao {

    void create(Ranking ranking);

    Ranking find(Tournament tournament, User user);

    List<Ranking> findByTournament(Tournament tournament);

    List<Ranking> findByUser(User user);

    void update(Ranking ranking);

    void delete(Ranking ranking);

}
