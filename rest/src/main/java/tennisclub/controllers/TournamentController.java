package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.ranking.RankingWithPlayerDTO;
import tennisclub.dto.tournament.TournamentCreateDTO;
import tennisclub.dto.tournament.TournamentDTO;
import tennisclub.dto.tournament.TournamentFullDTO;
import tennisclub.facade.TournamentFacade;

import java.util.List;

/**
 * @author Xuan Linh Phamová
 */
@CrossOrigin
@RestController
@RequestMapping("/tournaments")
public class TournamentController {
    private final TournamentFacade tournamentFacade;

    @Autowired
    public TournamentController(TournamentFacade tournamentFacade){
        this.tournamentFacade = tournamentFacade;
    }

    @GetMapping
    public List<TournamentFullDTO> getTournaments(){
        return tournamentFacade.getAllTournaments();
    }

    @GetMapping("/{id}")
    public TournamentFullDTO getTournamentById(@PathVariable Long id){
        return tournamentFacade.getTournamentWithId(id);
    }

    @PostMapping
    public TournamentDTO createTournament(@RequestBody TournamentCreateDTO tournamentDTO){
        return tournamentFacade.createTournament(tournamentDTO);
    }

    @PutMapping("/{tournamentId}/enroll-player")
    public RankingWithPlayerDTO enrollPlayer(@PathVariable Long tournamentId, @RequestParam Long playerId){
        return tournamentFacade.enrollPlayer(tournamentId, playerId);
    }

    @PutMapping("/{tournamentId}/withdraw-player")
    public void withdrawPlayer(@PathVariable Long tournamentId, @RequestParam Long playerId){
        tournamentFacade.withdrawPlayer(tournamentId, playerId);
    }

    @PutMapping("/{tournamentId}/rank-player")
    public RankingWithPlayerDTO rankPlayer(@PathVariable Long tournamentId, @RequestParam Long playerId, @RequestParam Integer playerPlacement){
        return tournamentFacade.rankPlayer(tournamentId, playerId, playerPlacement);
    }

    @DeleteMapping("/{id}")
    public void deleteTournament(@PathVariable Long id){
        tournamentFacade.deleteTournament(id);
    }
}
