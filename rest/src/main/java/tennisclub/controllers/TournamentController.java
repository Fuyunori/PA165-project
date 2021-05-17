package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.ranking.RankingWithPlayerDTO;
import tennisclub.dto.tournament.TournamentCreateDTO;
import tennisclub.dto.tournament.TournamentDTO;
import tennisclub.dto.tournament.TournamentFullDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.dto.user.UserFullDTO;
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
    public TournamentFullDTO createTournament(@RequestBody TournamentCreateDTO tournamentDTO){
        return tournamentFacade.createTournament(tournamentDTO);
    }

    @PostMapping("/{tournamentId}/users")
    public TournamentFullDTO enrollPlayer(@PathVariable Long tournamentId, @RequestBody UserFullDTO player){
        return tournamentFacade.enrollPlayer(tournamentId, player.getId());
    }

    @DeleteMapping("/{tournamentId}/users/{playerId}")
    public TournamentFullDTO withdrawPlayer(@PathVariable Long tournamentId, @PathVariable Long playerId){
        return tournamentFacade.withdrawPlayer(tournamentId, playerId);
    }

    @PutMapping("/{tournamentId}/rankings")
    public TournamentFullDTO rankPlayer(@PathVariable Long tournamentId, @RequestBody RankingWithPlayerDTO ranking){
        return tournamentFacade.rankPlayer(tournamentId, ranking.getPlayer().getId(), ranking.getPlayerPlacement());
    }

    @DeleteMapping("/{id}")
    public void deleteTournament(@PathVariable Long id){
        tournamentFacade.deleteTournament(id);
    }
}
