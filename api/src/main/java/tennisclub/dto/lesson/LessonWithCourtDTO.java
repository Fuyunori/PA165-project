package tennisclub.dto.lesson;

import java.time.LocalDateTime;
import java.util.Objects;

public class LessonWithCourtDTO {
    private Long id;
    // TODO: uncomment once the PR is merged :)
    // private CourtDto court;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer capacity;
    // TODO: uncomment once the PR is merged :)
    // private Level level;

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
        LessonWithCourtDTO lessonDTO = (LessonWithCourtDTO) o;
        return // Objects.equals(court, lessonDTO.getCourt());
                Objects.equals(startTime, lessonDTO.getStartTime())
                && Objects.equals(endTime, lessonDTO.getEndTime());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // result = prime * result  + ((court == null) ? 0 : court.hashCode());
        result = prime * result  + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result  + ((endTime == null) ? 0 : endTime.hashCode());
        return result;
    }
}
