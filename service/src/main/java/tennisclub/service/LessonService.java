package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Court;
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

    void enrollStudent(Lesson lesson, User student);

    void addTeacher(Lesson lesson, User teacher);
    void withdrawStudent(Lesson lesson, User student);

    void removeTeacher(Lesson lesson, User teacher);
    void remove(Lesson lesson);

    Lesson listById(Long id);
    List<Lesson> listAll();
    List<Lesson> listByCourt(Court court);
    List<Lesson> listByStartTime(LocalDateTime startTime);
    List<Lesson> listByEndTime(LocalDateTime endTime);
    List<Lesson> listByTimeInterval(LocalDateTime from, LocalDateTime to);
    List<Lesson> listAllLessonsToday();
    Set<Lesson> listByTeacher(User teacher);
    Set<Lesson> listByStudent(User student);
    List<Lesson> listByCapacity(Integer capacity);
    List<Lesson> listByLevel(Level level);
}
