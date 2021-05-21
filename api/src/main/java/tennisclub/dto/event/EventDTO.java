package tennisclub.dto.event;

import tennisclub.enums.EventType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Miroslav Demek
 */
public class EventDTO {

    private EventType type;
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public EventDTO() { }

    public EventDTO(EventType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventDTO)) return false;
        EventDTO eventDTO = (EventDTO) o;
        return Objects.equals(startTime, eventDTO.startTime) &&
                Objects.equals(endTime, eventDTO.endTime) &&
                type.equals(eventDTO.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, type);
    }
}
