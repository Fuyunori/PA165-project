package tennisclub.dto;

import tennisclub.annotations.IsEndTimeAfterStartTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@IsEndTimeAfterStartTime(start = "startTime", end = "endTime")
public class LessonCreateDTO {
    // TODO: uncomment once the courtDTO is done :)
    // @NotNull
    // private CourtDto court;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startTime;

    @NotNull
    @Future
    private LocalDateTime endTime;

    private Integer capacity;

    // TODO: uncomment once the PR is merged :)
    // private Level level;

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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonCreateDTO lessonDTO = (LessonCreateDTO) o;
        return // Objects.equals(court, lessonDTO.court) &&
                Objects.equals(startTime, lessonDTO.getStartTime())
                && Objects.equals(endTime, lessonDTO.getEndTime());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        //result = prime * result + ((court == null) ? 0 : court.hashCode());
        result = prime * result  + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result  + ((endTime == null) ? 0 : endTime.hashCode());
        return result;
    }
}
