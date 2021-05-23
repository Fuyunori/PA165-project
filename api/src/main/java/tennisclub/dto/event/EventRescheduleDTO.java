package tennisclub.dto.event;

import tennisclub.annotations.IsEndTimeAfterStartTime;
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
public class EventRescheduleDTO {

    private EventType type;

    @NotNull(message = "{event.time.start.notnull}")
    @FutureOrPresent(message = "{event.time.start.futureOrPresent}")
    private LocalDateTime startTime;

    @NotNull(message = "{event.time.end.notnull}")
    @Future(message = "{event.time.end.future}")
    private LocalDateTime endTime;

    public EventRescheduleDTO() { }

    public EventRescheduleDTO(EventType type) {
        this.type = type;
    }

    public EventRescheduleDTO(@NotNull @FutureOrPresent LocalDateTime startTime, @NotNull @Future LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventRescheduleDTO)) return false;
        EventRescheduleDTO that = (EventRescheduleDTO) o;
        return Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, type);
    }
}
