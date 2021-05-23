package tennisclub.dto.lesson;

import tennisclub.dto.event.EventCreateDTO;
import tennisclub.enums.EventType;
import tennisclub.enums.Level;

import javax.validation.constraints.NotNull;

/**
 * DTO for creating lessons
 * @author Xuan Linh Phamová
 */
public class LessonCreateDTO extends EventCreateDTO {
    private Integer capacity;

    @NotNull(message = "{lesson.level.notNull}")
    private Level level;

    public LessonCreateDTO() {
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
