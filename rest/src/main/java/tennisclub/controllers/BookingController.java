package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.booking.BookingCreateDTO;
import tennisclub.dto.booking.BookingFullDTO;
import tennisclub.dto.booking.BookingUpdateDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.enums.Role;
import tennisclub.exceptions.UnauthorisedException;
import tennisclub.facade.BookingFacade;
import tennisclub.facade.EventFacade;
import tennisclub.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Miroslav Demek
 */
@CrossOrigin
@RestController
@RequestMapping("/rest/bookings")
public class BookingController {

    private final BookingFacade bookingFacade;
    private final EventFacade eventFacade;
    private final UserService userService;

    @Autowired
    public BookingController(BookingFacade bookingFacade, EventFacade eventFacade, UserService userService) {
        this.bookingFacade = bookingFacade;
        this.eventFacade = eventFacade;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<BookingFullDTO>> getBookings(@RequestHeader("Authorization") String jwt) {
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(bookingFacade.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingFullDTO> getBookingById(@PathVariable Long id,
                                                         @RequestHeader("Authorization") String jwt) {
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(bookingFacade.findById(id));
    }

    @PostMapping
    public ResponseEntity<BookingFullDTO> createBooking(@Valid @RequestBody BookingCreateDTO createDTO,
                                                        @RequestHeader("Authorization") String jwt) {
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingFacade.makeBooking(createDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id, @RequestHeader("Authorization") String jwt) {
        userService.verifyRole(jwt, Role.USER);
        if (!bookingFacade.userIsAuthor(id, userService.getUserIdFromToken(jwt))) {
            throw new UnauthorisedException();
        }
        bookingFacade.cancelBooking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingFullDTO> putBooking(@PathVariable Long id,
                                                     @Valid @RequestBody BookingUpdateDTO dto,
                                                     @RequestHeader("Authorization") String jwt) {
        userService.verifyRole(jwt, Role.USER);
        if (!bookingFacade.userIsAuthor(id, userService.getUserIdFromToken(jwt))) {
            throw new UnauthorisedException();
        }
        eventFacade.reschedule(id, dto);
        return ResponseEntity.ok(bookingFacade.update(id, dto));
    }
}
