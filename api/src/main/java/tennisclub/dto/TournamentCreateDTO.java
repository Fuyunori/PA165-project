package tennisclub.dto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class TournamentCreateDTO {
    @NotNull
    private CourtDto court;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startTime;

    @NotNull
    @Future
    private LocalDateTime endTime;

    @NotBlank
    private String name;

    @Min(0)
    private Integer capacity;

    @NotNull
    @Min(0)
    private Integer prize;

    public CourtDto getCourt() {
        return court;
    }

    public void setCourt(CourtDto court) {
        this.court = court;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return Objects.equals(court, tournamentDTO.getCourt()) &&
                Objects.equals(startTime, tournamentDTO.getStartTime())
                && Objects.equals(endTime, tournamentDTO.getEndTime());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result  + ((court == null) ? 0 : court.hashCode());
        result = prime * result  + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result  + ((endTime == null) ? 0 : endTime.hashCode());
        return result;
    }
}
