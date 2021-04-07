package tennisclub.entity;

import com.sun.istack.NotNull;
import tennisclub.entity.enums.CourtType;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"name", "address"})
)
public class Court {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "address")
    private String address;
    private CourtType type;
    private String previewImage;

    @OneToMany(mappedBy = "court")
    private Set<Event> events = new HashSet<>();

    public Court() {}

    public Court(String name) {
        this.setName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String location) {
        this.address = location;
    }

    public CourtType getType() {
        return type;
    }

    public void setType(CourtType type) {
        this.type = type;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public Set<Event> getEvents() {
        return Collections.unmodifiableSet(events);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Court)) {
            return false;
        }
        Court otherCourt = (Court) obj;
        String name = getName();
        return this == otherCourt || name != null && name.equals(otherCourt.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
