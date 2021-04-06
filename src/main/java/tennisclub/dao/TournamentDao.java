package tennisclub.dao;

import tennisclub.entity.Court;
import tennisclub.entity.Event;
import tennisclub.entity.Tournament;

import java.time.LocalDateTime;
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
     * @return the updated tournament
     */
    Tournament update(Tournament tournament);

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
     * Finds all Tournaments taking place on the specified court.
     * @param court on which the Tournaments take place
     * @return all Tournaments taking place on the given court
     */
    List<Tournament> findByCourt(Court court);

    /**
     * Finds all Tournaments starting at the specified time.
     * @param startTime at which the Tournaments start
     * @return all Tournaments starting at the given time
     */
    List<Tournament> findByStartTime(LocalDateTime startTime);

    /**
     * Finds all Tournaments ending at the specified time.
     * @param endTime at which the Tournaments end
     * @return all Tournaments ending at the given time
     */
    List<Tournament> findByEndTime(LocalDateTime endTime);

    /**
     * Finds all Tournaments that at least partially take place
     * during the specified time interval. The interval is inclusive.
     *
     * More formally, retrieve all Tournaments t such that:
     *     t.startTime <= to && t.endTime >= from
     *
     * The behaviour of this method is undefined if:
     *     from > to
     *
     * @param from the beginning of the interval
     * @param to the end of the interval
     * @return all Tournaments whose time falls into the interval
     */
    List<Tournament> findByTimeInterval(LocalDateTime from, LocalDateTime to);

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
