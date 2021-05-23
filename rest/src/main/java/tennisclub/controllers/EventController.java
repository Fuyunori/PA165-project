package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.event.EventRescheduleDTO;
import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.enums.Role;
import tennisclub.facade.EventFacade;
import tennisclub.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Miroslav Demek
 */
@CrossOrigin
@RestController
@RequestMapping("/rest/events")
public class EventController {

    private final EventFacade eventFacade;
    private final UserService userService;

    @Autowired
    public EventController(EventFacade eventFacade, UserService userService) {
        this.eventFacade = eventFacade;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<EventWithCourtDTO>> getEvents(@RequestHeader(value = "Authorization", required = false) String jwt) {
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(eventFacade.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventWithCourtDTO> getEventById(@PathVariable Long id,
                                                          @RequestHeader(value = "Authorization", required = false) String jwt) {
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(eventFacade.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventWithCourtDTO> reschedule(@PathVariable Long id,
                                                        @Valid @RequestBody EventRescheduleDTO rescheduleDTO,
                                                        @RequestHeader(value = "Authorization", required = false) String jwt) {
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(eventFacade.reschedule(id, rescheduleDTO));
    }
}
