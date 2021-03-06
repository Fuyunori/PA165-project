package tennisclub.dto.booking;

import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.enums.EventType;

import java.util.Set;

/**
 * Booking DTO which contains references both to its court
 * and users that made the booking.
 *
 * @author Miroslav Demek
 */
public class BookingFullDTO extends EventWithCourtDTO {

    private Set<UserDTO> users;
    private UserDTO author;

    public BookingFullDTO() {
        super(EventType.BOOKING);
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }
}
