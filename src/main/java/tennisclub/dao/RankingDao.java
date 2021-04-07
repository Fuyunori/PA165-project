package tennisclub.dao;

import org.springframework.stereotype.Repository;
import tennisclub.entity.ranking.Ranking;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;

import java.util.List;

/**
 * DAO interface for CRUD operations on Ranking
 * @author Ondrej Holub
 */
@Repository
public interface RankingDao {

    /**
     * Persists a new Ranking
     * @param ranking which is to be persisted
     */
    void create(Ranking ranking);

    /**
     * Finds a unique Ranking by its primary key composed of
     * @param tournament part and
     * @param user part
     *
     * @return matching Ranking
     */
    Ranking find(Tournament tournament, User user);

    /**
     * Finds Rankings of a particular tournament
     * @param tournament that the rankings are part of
     * @return list of Rankings of the tournament
     */
    List<Ranking> findByTournament(Tournament tournament);

    /**
     * Finds Rankings of a particular user
     * @param user that has rankings
     * @return list of Rankings of the user
     */
    List<Ranking> findByUser(User user);

    /**
     * Updates a Ranking
     * @param ranking to be updated
     */
    void update(Ranking ranking);

    /**
     * Delete a Ranking
     * @param ranking to be deleted
     */
    void delete(Ranking ranking);

}
