package tennisclub.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Court {
    @Id
    @GeneratedValue
    private Long id;

    private String address;

    @OneToMany(mappedBy = "court")
    private Set<Event> events = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String location) {
        this.address = location;
    }

    public Set<Event> getEvents() {
        return Collections.unmodifiableSet(events);
    }

    public void addEvent(Event event) {
        events.add(event);
        event.setCourt(this);
    }

    @Override
    public boolean equals(Object obj) {
        // TODO implement properly
        return obj instanceof Court && Objects.equals(id, ((Court) obj).id);
    }

    @Override
    public int hashCode() {
        // TODO implement properly
        return Objects.hash(id);
    }
}