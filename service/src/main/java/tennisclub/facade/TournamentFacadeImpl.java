package tennisclub.facade;

import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dto.tournament.TournamentCreateDTO;
import tennisclub.dto.tournament.TournamentFullDTO;
import tennisclub.entity.Court;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;
import tennisclub.entity.ranking.Ranking;
import tennisclub.service.CourtService;
import tennisclub.service.TournamentService;
import tennisclub.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TournamentFacadeImpl implements TournamentFacade {
    private final Mapper mapper;
    private final TournamentService tournamentService;
    private final UserService userService;
    private final CourtService courtService;

    @Autowired
    public TournamentFacadeImpl(Mapper mapper,
                                TournamentService tournamentService,
                                UserService userService,
                                CourtService courtService){
        this.mapper = mapper;
        this.tournamentService = tournamentService;
        this.userService = userService;
        this.courtService = courtService;
    }

    @Override
    public Long createTournament(TournamentCreateDTO tournamentDTO) {
        Tournament tournament = mapper.map(tournamentDTO, Tournament.class);

        if (!courtService.isFree(tournament.getCourt(), tournament.getStartTime(), tournament.getEndTime())) {
            throw new SecurityException("Can't make a tournament. Court is not free at this time.");
        }

        Tournament newTournament = tournamentService.create(tournament);
        return newTournament.getId();
    }

    @Override
    public void deleteTournament(Long tournamentId) {
        Tournament tournament = tournamentService.findById(tournamentId);
        tournamentService.remove(tournament);
    }

    @Override
    public void addPlayerParticipation(Long tournamentId, Long playerId) {
        Tournament tournament = tournamentService.findById(tournamentId);
        User player = userService.findUserById(playerId);

        Ranking ranking = new Ranking(tournament, player);
        //rankingService.create(ranking);
    }

    @Override
    public void withdrawPlayerParticipation(Long tournamentId, Long playerId) {
        Tournament tournament = tournamentService.findById(tournamentId);
        User player = userService.findUserById(playerId);

        //Ranking ranking = rankingService.find(tournament, player);
        //rankingService.remove(ranking);
    }

    @Override
    public void rankPlayer(Long tournamentId, Long playerId, int placement) {
        Tournament tournament = tournamentService.findById(tournamentId);
        User player = userService.findUserById(playerId);

        //Ranking ranking = rankingService.find(tournament, player);
        //rankingService.updatePlacement(ranking, placement);
    }

    @Override
    public List<TournamentFullDTO> getRankingsOfTournament(Long tournamentId) {
        Tournament tournament = tournamentService.findById(tournamentId);
        Set<Ranking> rankings = tournament.getRankings();
        return rankings.stream()
                .map(e -> mapper.map(e, TournamentFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TournamentFullDTO getTournamentWithId(Long id) {
        Tournament tournament = tournamentService.findById(id);
        return (tournament == null) ? null : mapper.map(tournament, TournamentFullDTO.class);
    }

    @Override
    public List<TournamentFullDTO> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.findAll();
        return tournaments.stream()
                .map(e -> mapper.map(e, TournamentFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TournamentFullDTO> getTournamentsByStartTime(LocalDateTime startTime) {
        List<Tournament> tournaments = tournamentService.findByStartTime(startTime);
        return tournaments.stream()
                .map(e -> mapper.map(e, TournamentFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TournamentFullDTO> getTournamentsByEndTime(LocalDateTime endTime) {
        List<Tournament> tournaments = tournamentService.findByEndTime(endTime);
        return tournaments.stream()
                .map(e -> mapper.map(e, TournamentFullDTO.class))
                .collect(Collectors.toList());
    }
}
