package tennisclub.dto.booking;

import tennisclub.annotations.IsEndTimeAfterStartTime;
import tennisclub.dto.court.CourtDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
}
