package tennisclub.service;

import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.entity.enums.Level;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonService {
    void create(Lesson lesson);
    Lesson update(Lesson lesson);
    void remove(Lesson lesson);
    List<Lesson> listByStartTime(LocalDateTime startTime);
    List<Lesson> listByEndTime(LocalDateTime endTime);
    List<Lesson> listByTimeInterval(LocalDateTime from, LocalDateTime to);
    List<Lesson> listByCapacity(Integer capacity);
    List<Lesson> listByLevel(Level level);
}
