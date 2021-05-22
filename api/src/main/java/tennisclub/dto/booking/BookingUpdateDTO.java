package tennisclub.dto.booking;

import tennisclub.dto.event.EventRescheduleDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.enums.EventType;

import java.util.Set;

public class BookingUpdateDTO extends EventRescheduleDTO  {

    private Set<UserDTO> users;

    public BookingUpdateDTO() { }

    public BookingUpdateDTO(EventType type) {
        super(type);
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }


}
