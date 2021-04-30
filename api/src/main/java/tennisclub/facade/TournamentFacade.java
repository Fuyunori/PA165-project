package tennisclub.facade;

import tennisclub.dto.ranking.RankingWithPlayerDTO;
import tennisclub.dto.tournament.TournamentCreateDTO;
import tennisclub.dto.tournament.TournamentFullDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Xuan Linh Phamová
 */

/**
 * Facade layer for Tournament.
 */
public interface TournamentFacade {
    /**
     * Creates a tournament. The method is successful if the court if free in the given time interval.
     * Otherwise throws a FacadeLayerException.
     * @param tournamentDTO DTO that is mapped to the entity
     * @return id of the newly created tournament
     */
    Long createTournament(TournamentCreateDTO tournamentDTO);

    /**
     * Deletes a tournament.
     * @param tournamentId of the tournament to be deleted
     */
    void deleteTournament(Long tournamentId);

    /**
     * Enrolls player into a tournament. It is possible only in the enrollment period
     * and if the player isn't a participant yet.
     * Otherwise an exception from the service layer is thrown.
     * @param tournamentId of the tournament
     * @param playerId of the player
     */
    void enrollPlayer(Long tournamentId, Long playerId);

    /**
     * Withdraws player from a tournament. It is possible only in the enrollment period
     * and if the player is a participant.
     * Otherwise an exception from the service layer is thrown.
     * @param tournamentId of the tournament
     * @param playerId of the participant
     */
    void withdrawPlayer(Long tournamentId, Long playerId);

    /**
     * Assigns a rank to the player. It is possible only if the participant participates in the tournament
     * and if the rank doesn't exceed the capacity.
     * Otherwise an exception from the service layer is thrown.
     * @param tournamentId of the tournament
     * @param playerId of the user.
     * @param placement of the user.
     */
    void rankPlayer(Long tournamentId, Long playerId, int placement);

    /**
     * Get rankings of a tournament.
     * @param tournamentId of the tournament
     * @return list of rankings
     */
    List<RankingWithPlayerDTO> getRankingsOfTournament(Long tournamentId);

    /**
     * Retrieves tournament by id mapped DTO tournament.
     * @param id of the tournament
     * @return mapped DTO tournament
     */
    TournamentFullDTO getTournamentWithId(Long id);

    /**
     * Retrieves all tournaments mapped DTO.
     * @return list of all mapped tournament DTOs
     */
    List<TournamentFullDTO> getAllTournaments();

    /**
     * Retrieves all tournaments by start time.
     * @param startTime of the tournament
     * @return list of all tournaments with the specified start time mapped to DTOs
     */
    List<TournamentFullDTO> getTournamentsByStartTime(LocalDateTime startTime);

    /**
     * Retrieves all tournaments by end time.
     * @param endTime of the tournament
     * @return list of all tournaments with the specified end time mapped to DTOs
     */
    List<TournamentFullDTO> getTournamentsByEndTime(LocalDateTime endTime);
}
