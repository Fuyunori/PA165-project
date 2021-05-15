package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.court.CourtCreateDto;
import tennisclub.dto.court.CourtDto;
import tennisclub.dto.court.CourtUpdateDto;
import tennisclub.facade.CourtFacade;

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
    public List<CourtDto> getCourts() {
        return courtFacade.listAll();
    }

    @GetMapping("/{id}")
    public CourtDto getCourtById(@PathVariable Long id) {
        return courtFacade.getById(id);
    }

    @PostMapping
    public CourtDto getCourts(@RequestBody CourtCreateDto dto) {
        return courtFacade.create(dto);
    }

    @PutMapping("/{id}")
    public CourtDto putCourt(@PathVariable Long id, @RequestBody CourtUpdateDto dto) {
        return courtFacade.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCourt(@PathVariable Long id) {
        courtFacade.delete(id);
    }
}
