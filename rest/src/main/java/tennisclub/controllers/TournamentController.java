package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.ranking.RankingWithPlayerDTO;
import tennisclub.dto.tournament.TournamentCreateDTO;
import tennisclub.dto.tournament.TournamentDTO;
import tennisclub.dto.tournament.TournamentFullDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.dto.user.UserFullDTO;
import tennisclub.facade.TournamentFacade;

import javax.validation.Valid;
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
    public ResponseEntity<List<TournamentFullDTO>> getTournaments(){
        return ResponseEntity.ok(tournamentFacade.getAllTournaments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentFullDTO> getTournamentById(@PathVariable Long id){
        return ResponseEntity.ok(tournamentFacade.getTournamentWithId(id));
    }

    @PostMapping
    public ResponseEntity<TournamentFullDTO> createTournament(@Valid @RequestBody TournamentCreateDTO tournamentDTO){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tournamentFacade.createTournament(tournamentDTO));
    }

    @PostMapping("/{tournamentId}/users")
    public ResponseEntity<TournamentFullDTO> enrollPlayer(@PathVariable Long tournamentId, @RequestBody UserFullDTO player){
        return ResponseEntity.ok(tournamentFacade.enrollPlayer(tournamentId, player.getId()));
    }

    @DeleteMapping("/{tournamentId}/users/{playerId}")
    public ResponseEntity<TournamentFullDTO> withdrawPlayer(@PathVariable Long tournamentId, @PathVariable Long playerId){
        return ResponseEntity.ok(tournamentFacade.withdrawPlayer(tournamentId, playerId));
    }

    @PutMapping("/{tournamentId}/rankings")
    public ResponseEntity<TournamentFullDTO> rankPlayer(@PathVariable Long tournamentId, @Valid @RequestBody RankingWithPlayerDTO ranking){
        return ResponseEntity.ok(tournamentFacade.rankPlayer(tournamentId, ranking.getPlayer().getId(), ranking.getPlayerPlacement()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id){
        tournamentFacade.deleteTournament(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
