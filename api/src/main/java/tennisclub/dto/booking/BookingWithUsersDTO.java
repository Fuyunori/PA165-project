package tennisclub.dto.booking;

import tennisclub.dto.event.EventDTO;
import tennisclub.dto.user.UserDTO;

import java.util.Set;

/**
 * @author Miroslav Demek
 */
public class BookingWithUsersDTO extends EventDTO {

    private Set<UserDTO> users;
    private UserDTO author;

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
