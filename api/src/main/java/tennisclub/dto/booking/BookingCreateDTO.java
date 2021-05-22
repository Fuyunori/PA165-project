package tennisclub.dto.booking;

import tennisclub.dto.event.EventCreateDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.enums.EventType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Miroslav Demek
 */
public class BookingCreateDTO extends EventCreateDTO {
    
    private Set<UserDTO> users;

    @NotNull(message = "{booking.author.notnull}")
    private UserDTO author;

    public BookingCreateDTO() {
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
