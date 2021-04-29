package tennisclub.dto.event;

import tennisclub.annotations.IsEndTimeAfterStartTime;
import tennisclub.dto.court.CourtDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@IsEndTimeAfterStartTime(start = "startTime", end = "endTime")
public abstract class EventCreateDTO {

    @NotNull
    @FutureOrPresent
    private LocalDateTime startTime;

    @NotNull
    @Future
    private LocalDateTime endTime;

    @NotNull
    private CourtDto court;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventCreateDTO)) return false;
        EventCreateDTO that = (EventCreateDTO) o;
        return startTime.equals(that.startTime) && endTime.equals(that.endTime) && court.equals(that.court);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, court);
    }
}
