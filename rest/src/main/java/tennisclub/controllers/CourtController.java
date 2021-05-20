package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.court.CourtCreateDto;
import tennisclub.dto.court.CourtDto;
import tennisclub.dto.court.CourtUpdateDto;
import tennisclub.facade.CourtFacade;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Pavel Tobiáš
 */
@CrossOrigin
@RestController
@RequestMapping("/courts")
public class CourtController {
    private final CourtFacade courtFacade;

    @Autowired
    public CourtController(CourtFacade courtFacade) {
        this.courtFacade = courtFacade;
    }

    @GetMapping
    public ResponseEntity<List<CourtDto>> getCourts() {
        List<CourtDto> courts = courtFacade.listAll();
        return ResponseEntity.status(200).body(courts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourtDto> getCourtById(@PathVariable Long id) {
        CourtDto court = courtFacade.getById(id);
        return ResponseEntity.status(200).body(court);
    }

    @PostMapping
    public ResponseEntity<CourtDto> postCourt(@Valid @RequestBody CourtCreateDto dto) {
        CourtDto court = courtFacade.create(dto);
        return ResponseEntity.status(201).body(court);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourtDto> putCourt(@PathVariable Long id, @Valid @RequestBody CourtUpdateDto dto) {
        CourtDto court = courtFacade.update(id, dto);
        return ResponseEntity.status(200).body(court);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourt(@PathVariable Long id) {
        courtFacade.delete(id);
        return ResponseEntity.status(204).build();
    }
}
