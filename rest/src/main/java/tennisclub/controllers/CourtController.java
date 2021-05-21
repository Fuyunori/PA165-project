package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.court.CourtCreateDto;
import tennisclub.dto.court.CourtDto;
import tennisclub.dto.court.CourtUpdateDto;
import tennisclub.dto.event.EventDTO;
import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.enums.Role;
import tennisclub.facade.CourtFacade;
import tennisclub.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Pavel Tobiáš
 */
@CrossOrigin
@RestController
@RequestMapping("/rest/courts")
public class CourtController {
    private final CourtFacade courtFacade;
    private final UserService userService;

    @Autowired
    public CourtController(CourtFacade courtFacade, UserService userService) {
        this.courtFacade = courtFacade;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<CourtDto>> getCourts(
            @RequestHeader("Authorization") String jwt
    ) {
        userService.verifyRole(jwt, Role.USER);
        List<CourtDto> courts = courtFacade.listAll();
        return ResponseEntity.status(200).body(courts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourtDto> getCourtById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) {
        userService.verifyRole(jwt, Role.USER);
        CourtDto court = courtFacade.getById(id);
        return ResponseEntity.status(200).body(court);
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<List<EventWithCourtDTO>> getCourtEvents(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) {
        userService.verifyRole(jwt, Role.USER);
        List<EventWithCourtDTO> events = courtFacade.listCourtEvents(id);
        return ResponseEntity.status(200).body(events);
    }

    @PostMapping
    public ResponseEntity<CourtDto> postCourt(
            @Valid @RequestBody CourtCreateDto dto,
            @RequestHeader("Authorization") String jwt
    ) {
        userService.verifyRole(jwt, Role.MANAGER);
        CourtDto court = courtFacade.create(dto);
        return ResponseEntity.status(201).body(court);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourtDto> putCourt(
            @PathVariable Long id,
            @Valid @RequestBody CourtUpdateDto dto,
            @RequestHeader("Authorization") String jwt
    ) {
        userService.verifyRole(jwt, Role.MANAGER);
        CourtDto court = courtFacade.update(id, dto);
        return ResponseEntity.status(200).body(court);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourt(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) {
        userService.verifyRole(jwt, Role.MANAGER);
        courtFacade.delete(id);
        return ResponseEntity.status(204).build();
    }
}
