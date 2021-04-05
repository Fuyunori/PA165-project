package tennisclub.entity;

import javax.persistence.*;
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

    private CourtType type;

    // TODO remove eager fetching after figuring out how to do without it
    @OneToMany(mappedBy = "court", fetch = FetchType.EAGER)
    private Set<Event> events = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public CourtType getType() {
        return type;
    }

    public void setType(CourtType type) {
        this.type = type;
    }

    public void setAddress(String location) {
        this.address = location;
    }

    public Set<Event> getEvents() {
        return Collections.unmodifiableSet(events);
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