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
@IsEndTimeAfterStartTime(start = "start", end = "end", message = "{event.time.isEndAfterStart}")
public class EventRescheduleDTO {

    private EventType type;

    @NotNull(message = "{event.time.start.notnull}")
    @FutureOrPresent(message = "{event.time.start.futureOrPresent}")
    private LocalDateTime start;

    @NotNull(message = "{event.time.end.notnull}")
    @Future(message = "{event.time.end.future}")
    private LocalDateTime end;

    public EventRescheduleDTO() { }

    public EventRescheduleDTO(EventType type) {
        this.type = type;
    }

    public EventRescheduleDTO(@NotNull @FutureOrPresent LocalDateTime start, @NotNull @Future LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventRescheduleDTO)) return false;
        EventRescheduleDTO that = (EventRescheduleDTO) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, type);
    }
}
