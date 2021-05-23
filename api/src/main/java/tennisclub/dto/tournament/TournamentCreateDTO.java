package tennisclub.dto.tournament;

import tennisclub.dto.court.CourtDto;
import tennisclub.dto.event.EventCreateDTO;
import tennisclub.enums.EventType;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Xuan Linh Phamová
 */
public class TournamentCreateDTO extends EventCreateDTO {
    @NotBlank(message = "{tournament.name.notBlank}")
    private String name;

    @Min(value = 0, message = "{tournament.capacity.min}")
    private Integer capacity;

    @NotNull(message = "{tournament.prize.notNull}")
    @Min(value = 0, message = "{tournament.prize.min}")
    private Integer prize;

    public TournamentCreateDTO() {
        super(EventType.TOURNAMENT);
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
}
