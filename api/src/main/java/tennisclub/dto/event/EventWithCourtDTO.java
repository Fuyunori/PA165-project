package tennisclub.dto.event;

import tennisclub.dto.court.CourtDto;
import tennisclub.enums.EventType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Miroslav Demek
 */
public class EventWithCourtDTO {

    private EventType type;
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private CourtDto court;

    public EventWithCourtDTO() { }

    public EventWithCourtDTO(EventType type) {
        this.type = type;
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

    public CourtDto getCourt() {
        return court;
    }

    public void setCourt(CourtDto court) {
        this.court = court;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(o instanceof EventWithCourtDTO)) return false;
        EventWithCourtDTO that = (EventWithCourtDTO) o;
        return Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(court, that.court) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, court, type);
    }
}
