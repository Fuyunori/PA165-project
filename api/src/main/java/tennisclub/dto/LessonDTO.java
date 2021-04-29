package tennisclub.dto;

import tennisclub.enums.Level;

import java.time.LocalDateTime;
import java.util.Objects;

public class LessonDTO extends EventDTO {
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
