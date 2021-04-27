package tennisclub.dto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

public class TournamentCreateDTO {
    @NotNull
    private Long courtId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startTime;

    @NotNull
    @Future
    private LocalDateTime endTime;

    @Min(0)
    private Integer capacity;

    @NotNull
    @Min(0)
    private Integer prize;

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
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

    public Integer getPrize() {
        return prize;
    }

    public void setPrize(Integer prize) {
        this.prize = prize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TournamentCreateDTO tournamentDTO = (TournamentCreateDTO) o;
        return Objects.equals(startTime, tournamentDTO.getStartTime())
                && Objects.equals(endTime, tournamentDTO.getEndTime());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result  + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result  + ((endTime == null) ? 0 : endTime.hashCode());
        return result;
    }
}
