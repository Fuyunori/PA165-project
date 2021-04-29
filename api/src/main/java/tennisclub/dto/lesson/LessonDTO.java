package tennisclub.dto.lesson;

import tennisclub.dto.event.EventDTO;
import tennisclub.enums.Level;

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
