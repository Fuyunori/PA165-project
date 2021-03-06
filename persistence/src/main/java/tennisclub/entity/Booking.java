package tennisclub.entity;

import javax.persistence.*;
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
    @JoinTable(
        name = "user_booking",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private User author;

    public Booking() { }

    public Booking(LocalDateTime startTime, LocalDateTime endTime) {
        super(startTime, endTime);
    }

    public Set<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
