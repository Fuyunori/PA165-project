package tennisclub.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Miroslav Demek
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne
    private Court court;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    public Event() { }

    public Event(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(getCourt(), event.getCourt()) &&
                Objects.equals(getStartTime(), event.getStartTime()) &&
                Objects.equals(getEndTime(), event.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourt(), getStartTime(), getEndTime());
    }
}
