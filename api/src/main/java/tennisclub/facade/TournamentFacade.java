package tennisclub.facade;

import tennisclub.dto.ranking.TournamentRankingDTO;
import tennisclub.dto.tournament.TournamentCreateDTO;
import tennisclub.dto.tournament.TournamentFullDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface TournamentFacade {
    Long createTournament(TournamentCreateDTO tournamentDTO);
    void deleteTournament(Long tournamentId);

    void addPlayerParticipation(Long tournamentId, Long playerId);
    void withdrawPlayerParticipation(Long tournamentId, Long playerId);

    void rankPlayer(Long tournamentId, Long playerId, Integer placement);
    List<TournamentRankingDTO> getRankingsOfTournament(Long tournamentId);

    TournamentFullDTO getTournamentWithId(Long id);
    List<TournamentFullDTO> getAllTournaments();
    List<TournamentFullDTO> getTournamentsByStartTime(LocalDateTime startTime);
    List<TournamentFullDTO> getTournamentsByEndTime(LocalDateTime endTime);
}
