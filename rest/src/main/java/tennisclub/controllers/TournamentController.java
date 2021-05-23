package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.event.EventRescheduleDTO;
import tennisclub.dto.lesson.LessonFullDTO;
import tennisclub.dto.ranking.RankingWithPlayerDTO;
import tennisclub.dto.tournament.TournamentCreateDTO;
import tennisclub.dto.tournament.TournamentDTO;
import tennisclub.dto.tournament.TournamentFullDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.dto.user.UserFullDTO;
import tennisclub.enums.Role;
import tennisclub.facade.EventFacade;
import tennisclub.facade.TournamentFacade;
import tennisclub.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Xuan Linh Phamová
 */
@CrossOrigin
@RestController
@RequestMapping("/rest/tournaments")
public class TournamentController {
    private final TournamentFacade tournamentFacade;
    private final EventFacade eventFacade;
    private final UserService userService;

    @Autowired
    public TournamentController(TournamentFacade tournamentFacade,
                                EventFacade eventFacade,
                                UserService userService){
        this.tournamentFacade = tournamentFacade;
        this.eventFacade = eventFacade;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<TournamentFullDTO>> getTournaments(@RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(tournamentFacade.getAllTournaments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentFullDTO> getTournamentById(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(tournamentFacade.getTournamentWithId(id));
    }

    @PostMapping
    public ResponseEntity<TournamentFullDTO> createTournament(@Valid @RequestBody TournamentCreateDTO tournamentDTO, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.MANAGER);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tournamentFacade.createTournament(tournamentDTO));
    }

    @PostMapping("/{tournamentId}/players")
    public ResponseEntity<TournamentFullDTO> enrollPlayer(@PathVariable Long tournamentId, @RequestBody UserFullDTO player, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(tournamentFacade.enrollPlayer(tournamentId, player.getId()));
    }

    @DeleteMapping("/{tournamentId}/players/{playerId}")
    public ResponseEntity<TournamentFullDTO> withdrawPlayer(@PathVariable Long tournamentId, @PathVariable Long playerId, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(tournamentFacade.withdrawPlayer(tournamentId, playerId));
    }

    @PutMapping("/{tournamentId}/rankings")
    public ResponseEntity<TournamentFullDTO> rankPlayer(@PathVariable Long tournamentId, @Valid @RequestBody RankingWithPlayerDTO ranking, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.MANAGER);
        return ResponseEntity.ok(tournamentFacade.rankPlayer(tournamentId, ranking.getPlayer().getId(), ranking.getPlayerPlacement()));
    }

    @PutMapping("/{tournamentId}")
    public ResponseEntity<TournamentFullDTO> rescheduleLesson(@PathVariable Long tournamentId, @Valid @RequestBody EventRescheduleDTO dto, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.MANAGER);
        eventFacade.reschedule(tournamentId, dto);
        return ResponseEntity.ok(tournamentFacade.getTournamentWithId(tournamentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.USER);
        tournamentFacade.deleteTournament(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
