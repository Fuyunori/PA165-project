package tennisclub.dto.lesson;

import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.enums.EventType;
import tennisclub.enums.Level;

/**
 * DTO for lessons including relation with court
 * @author Xuan Linh Phamová
 */
public class LessonWithCourtDTO extends EventWithCourtDTO {
    private Integer capacity;
    private Level level;

    public LessonWithCourtDTO() {
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
