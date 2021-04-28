package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.entity.enums.Level;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public interface LessonService {
    Lesson create(Lesson lesson);
    Lesson update(Lesson lesson);
    void remove(Lesson lesson);
    Lesson listById(Long id);

    void enrollStudent(Lesson lesson, User student);
    void addTeacher(Lesson lesson, User teacher);

    void withdrawStudent(Lesson lesson, User student);
    void removeTeacher(Lesson lesson, User teacher);

    List<Lesson> listAll();
    List<Lesson> listByStartTime(LocalDateTime startTime);
    List<Lesson> listByEndTime(LocalDateTime endTime);
    List<Lesson> listByTimeInterval(LocalDateTime from, LocalDateTime to);
    List<Lesson> listAllLessonsToday();
    Set<Lesson> listByTeacher(User teacher);
    Set<Lesson> listByStudent(User student);
    List<Lesson> listByCapacity(Integer capacity);
    List<Lesson> listByLevel(Level level);
}
