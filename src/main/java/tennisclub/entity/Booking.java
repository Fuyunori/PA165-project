package tennisclub.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public void addUser(User user) {
        users.add(user);
        // TODO uncomment this code when User entity is finished
        // user.bookings.add(user);
    }
}
