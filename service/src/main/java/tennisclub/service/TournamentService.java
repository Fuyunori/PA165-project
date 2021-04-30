package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Court;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;
import tennisclub.entity.ranking.Ranking;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Xuan Linh Phamová
 */
@Service
public interface TournamentService {
    /**
     * Creates a tournament.
     * @param tournament to be created
     * @return the newly created tournament
     */
    Tournament create(Tournament tournament);

    /**
     * Updates a tournament.
     * @param tournament to be updated.
     * @return the newly updated tournament.
     */
    Tournament update(Tournament tournament);

    /**
     * Removes a tournament.
     * @param tournament to be removed
     */
    void remove(Tournament tournament);

    /**
     * Finds a tournament by id.
     * @param id of the tournament
     * @return the tournament
     */
    Tournament findById(Long id);

    /**
     * Finds all tournaments.
     * @return all tournaments
     */
    List<Tournament> findAll();

    /**
     * Finds all tournaments by court.
     * @param court of the tournament
     * @return all tournaments with the specified court
     */
    List<Tournament> findByCourt(Court court);

    /**
     * Finds all tournaments by start time.
     * @param startTime of the tournament
     * @return all tournaments with the specified start time
     */
    List<Tournament> findByStartTime(LocalDateTime startTime);

    /**
     * Finds all tournaments by end time.
     * @param endTime of the tournament
     * @return all tournaments with the specified end time
     */
    List<Tournament> findByEndTime(LocalDateTime endTime);

    /**
     * Finds all tournaments by the time interval.
     * @param from start of the interval
     * @param to end of the interval
     * @return all tournaments with the specified time interval
     */
    List<Tournament> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    /**
     * Finds all tournaments by the capacity.
     * @param capacity of the tournaments
     * @return all tournaments with the specified capacity
     */
    List<Tournament> findByCapacity(Integer capacity);

    /**
     * Finds all rankings by the specified tournament and player.
     * @param tournament the tournament
     * @param player the player
     * @return the ranking of the specified tournament and player
     */
    Ranking findRanking(Tournament tournament, User player);

    /**
     * Finds all rankings by tournament.
     * @param tournament the tournament
     * @return the rankings of the specified tournament
     */
    List<Ranking> findRankingByTournament(Tournament tournament);

    /**
     * Finds all rankings by player.
     * @param player the player
     * @return the rankings of the specified player
     */
    List<Ranking> findRankingByPlayer(User player);

    /**
     * Enrolls a player into a tournament.
     * @param tournament to be enrolled in
     * @param player to be enrolled in
     */
    void enrollPlayer(Tournament tournament, User player);

    /**
     * Withdraws a player from a tournament.
     * @param tournament to be withdrawn from
     * @param player to be withdrawn from
     */
    void withdrawPlayer(Tournament tournament, User player);

    /**
     * Ranks a player.
     * @param tournament tournament to be ranked in
     * @param player whose rank is going to change
     * @param newPlacement new placement of the player
     * @return the ranking
     */
    Ranking rankPlayer(Tournament tournament, User player, Integer newPlacement);
}
