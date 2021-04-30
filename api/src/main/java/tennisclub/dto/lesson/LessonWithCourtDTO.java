package tennisclub.dto.lesson;

import tennisclub.dto.court.CourtDto;
import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.enums.Level;

import java.time.LocalDateTime;
import java.util.Objects;

public class LessonWithCourtDTO extends EventWithCourtDTO {
    private Integer capacity;
    private Level level;

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
