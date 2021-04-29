package tennisclub.dto.event;

import tennisclub.dto.court.CourtDto;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventWithCourtDTO {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
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
        if (!(o instanceof EventWithCourtDTO)) return false;
        EventWithCourtDTO that = (EventWithCourtDTO) o;
        return Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(court, that.court);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, court);
    }
}
