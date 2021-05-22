package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.booking.BookingCreateDTO;
import tennisclub.dto.booking.BookingFullDTO;
import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.facade.BookingFacade;

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

    @Autowired
    public BookingController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping()
    public ResponseEntity<List<BookingFullDTO>> getBookings() {
        return ResponseEntity.ok(bookingFacade.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingFullDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingFacade.findById(id));
    }

    @PostMapping
    public ResponseEntity<BookingFullDTO> createBooking(@Valid @RequestBody BookingCreateDTO createDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingFacade.makeBooking(createDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingFacade.cancelBooking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/users")
    public ResponseEntity<BookingFullDTO> addUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(bookingFacade.addUser(id, userDTO.getId()));
    }

    @DeleteMapping("/{bookingId}/users/{userId}")
    public ResponseEntity<BookingFullDTO> removeUser(@PathVariable Long bookingId, @PathVariable Long userId) {
        return ResponseEntity.ok(bookingFacade.removeUser(bookingId, userId));
    }
}
