package tennisclub.dto.booking;

import tennisclub.dto.event.EventCreateDTO;
import tennisclub.dto.user.UserDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Miroslav Demek
 */
public class BookingCreateDTO extends EventCreateDTO {

    @NotEmpty
    private Set<UserDTO> users;

    @NotNull
    private UserDTO author;

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }
}
