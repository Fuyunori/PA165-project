package tennisclub.dto.lesson;

import tennisclub.annotations.IsEndTimeAfterStartTime;
import tennisclub.dto.event.EventCreateDTO;
import tennisclub.enums.Level;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

public class LessonCreateDTO extends EventCreateDTO {
    private Integer capacity;

    @NotNull
    private Level level;

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
