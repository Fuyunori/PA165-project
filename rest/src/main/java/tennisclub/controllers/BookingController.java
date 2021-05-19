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

import java.util.List;

/**
 * @author Miroslav Demek
 */
@CrossOrigin
@RestController
@RequestMapping("/bookings")
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
    public ResponseEntity<BookingFullDTO> createBooking(@RequestBody BookingCreateDTO createDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingFacade.makeBooking(createDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable String id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingFullDTO> addUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(bookingFacade.addUser(id, userDTO.getId()));
    }

    @DeleteMapping("/{bookingId}/users/{userId}")
    public ResponseEntity<BookingFullDTO> removeUser(@PathVariable Long bookingId, @PathVariable Long userId) {
        return ResponseEntity.ok(bookingFacade.removeUser(bookingId, userId));
    }
}
