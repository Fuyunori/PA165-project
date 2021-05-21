package tennisclub.dto.event;

import tennisclub.annotations.IsEndTimeAfterStartTime;
import tennisclub.dto.court.CourtDto;
import tennisclub.enums.EventType;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Miroslav Demek
 */
@IsEndTimeAfterStartTime(start = "startTime", end = "endTime", message = "{event.time.isEndAfterStart}")
public abstract class EventCreateDTO {

    private EventType type;

    @NotNull(message = "{event.time.start.notnull}")
    @FutureOrPresent(message = "{event.time.start.futureOrPresent}")
    private LocalDateTime startTime;

    @NotNull(message = "{event.time.end.notnull}")
    @Future(message = "{event.time.end.future}")
    private LocalDateTime endTime;

    @NotNull(message = "{event.court.notnull}")
    private CourtDto court;

    public EventCreateDTO() { }

    public EventCreateDTO(EventType type) {
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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventCreateDTO)) return false;
        EventCreateDTO that = (EventCreateDTO) o;
        return startTime.equals(that.startTime) &&
                endTime.equals(that.endTime) &&
                court.equals(that.court) &&
                type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, court, type);
    }
}
