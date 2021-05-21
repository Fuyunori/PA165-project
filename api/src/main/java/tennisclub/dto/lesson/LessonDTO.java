package tennisclub.dto.lesson;

import tennisclub.dto.event.EventDTO;
import tennisclub.enums.EventType;
import tennisclub.enums.Level;

/**
 * DTO for lessons without any relations
 * @author Xuan Linh Phamová
 */
public class LessonDTO extends EventDTO {
    private Integer capacity;
    private Level level;

    public LessonDTO() {
        super(EventType.LESSON);
    }

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
