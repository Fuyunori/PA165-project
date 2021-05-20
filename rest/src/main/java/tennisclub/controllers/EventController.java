package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.event.EventRescheduleDTO;
import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.facade.EventFacade;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Miroslav Demek
 */
@CrossOrigin
@RestController
@RequestMapping("/events")
public class EventController {

    private final EventFacade eventFacade;

    @Autowired
    public EventController(EventFacade eventFacade) {
        this.eventFacade = eventFacade;
    }

    @GetMapping()
    public ResponseEntity<List<EventWithCourtDTO>> getEvents() {
        return ResponseEntity.ok(eventFacade.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventWithCourtDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventFacade.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventWithCourtDTO> reschedule(@PathVariable Long id, @Valid @RequestBody EventRescheduleDTO rescheduleDTO) {
        return ResponseEntity.ok(eventFacade.reschedule(id, rescheduleDTO));
    }
}
