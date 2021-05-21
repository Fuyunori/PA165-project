package tennisclub.dto.booking;

import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.enums.EventType;

/**
 * Booking DTO containing a reference to its court but *not* the users which made it.
 *
 * It is an empty class since booking only holds its users. However, this class exists since
 * it makes the code working with booking DTOs more readable.
 *
 * @author Miroslav Demek
 */
public class BookingWithCourtDTO extends EventWithCourtDTO  {

    public BookingWithCourtDTO() {
        super(EventType.BOOKING);
    }
}
