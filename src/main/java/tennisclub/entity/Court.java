package tennisclub.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Court {
    @Id
    @GeneratedValue
    private Long id;

    private String address;

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