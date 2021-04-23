package tennisclub.service;

import tennisclub.entity.Lesson;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonService {
    void create(Lesson lesson);
    Lesson update(Lesson lesson);
    void remove(Lesson lesson);
    List<Lesson> listByStartTime(LocalDateTime startTime);
    List<Lesson> listByEndTime(LocalDateTime endTime);
}
