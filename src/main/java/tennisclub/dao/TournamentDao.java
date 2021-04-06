package tennisclub.dao;

import tennisclub.entity.Tournament;

import java.util.List;

/**
 * DAO interface for CRUD operations on Tournament.
 *
 * @author Xuan Linh Phamová
 */
public interface TournamentDao {
    /**
     * Persists a new Tournament.
     * @param tournament to be created
     */
    void create(Tournament tournament);

    /**
     * Updates a Tournament.
     * @param tournament to be updated
     */
    void update(Tournament tournament);

    /**
     * Removes a Tournament. In case the tournament is in detached state,
     * it is reattached and then removed.
     * @param tournament to be removed
     */
    void remove(Tournament tournament);

    /**
     * Retrieves all Tournaments.
     * @return all tournaments
     */
    List<Tournament> findAll();

    /**
     * Finds a particular Tournament by its id.
     * @param id of the searched tournament
     * @return a Tournament whose id matches the given id
     */
    Tournament findById(Long id);

    /**
     * Finds all Tournaments with particular capacity.
     * @param capacity of the tournaments
     * @return all Tournaments which have the given capacity
     */
    List<Tournament> findByCapacity(Integer capacity);

    /**
     * Finds all Tournaments with particular prize.
     * @param prize of the tournaments
     * @return all Tournaments which have the given prize
     */
    List<Tournament> findByPrize(Integer prize);
}
