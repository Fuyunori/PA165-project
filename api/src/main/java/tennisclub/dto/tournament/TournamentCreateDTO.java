package tennisclub.dto.tournament;

import tennisclub.dto.court.CourtDto;
import tennisclub.dto.event.EventCreateDTO;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Xuan Linh Phamová
 */
public class TournamentCreateDTO extends EventCreateDTO {
    @NotBlank
    private String name;

    @Min(0)
    private Integer capacity;

    @NotNull
    @Min(0)
    private Integer prize;

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
}
