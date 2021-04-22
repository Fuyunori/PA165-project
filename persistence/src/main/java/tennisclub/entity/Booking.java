package tennisclub.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Miroslav Demek
 */
@Entity
public class Booking extends Event {

    @ManyToMany
    private Set<User> users = new HashSet<>();

    public Booking() { }

    public Booking(LocalDateTime startTime, LocalDateTime endTime) {
        super(startTime, endTime);
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }
}
