package tennisclub.dto.booking;

import tennisclub.dto.event.EventCreateDTO;
import tennisclub.dto.user.UserDTO;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class BookingCreateDTO extends EventCreateDTO {
    @NotEmpty
    private Set<UserDTO> users;

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }
}
