package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.entity.enums.Level;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface LessonService {
    void create(Lesson lesson);
    Lesson update(Lesson lesson);
    void remove(Lesson lesson);

    void enrollStudent(Lesson lesson, User student);
    void addTeacher(Lesson lesson, User teacher);

    void withdrawStudent(Lesson lesson, User student);
    void removeTeacher(Lesson lesson, User teacher);

    Lesson listById(Long id);
    List<Lesson> listAll();
    List<Lesson> listByStartTime(LocalDateTime startTime);
    List<Lesson> listByEndTime(LocalDateTime endTime);
    List<Lesson> listByTimeInterval(LocalDateTime from, LocalDateTime to);
    List<Lesson> listByCapacity(Integer capacity);
    List<Lesson> listByLevel(Level level);
}
