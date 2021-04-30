package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.enums.Level;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public interface LessonService {
    Lesson create(Lesson lesson);
    Lesson update(Lesson lesson);
    void remove(Lesson lesson);

    Lesson enrollStudent(Lesson lesson, User student);
    Lesson withdrawStudent(Lesson lesson, User student);

    Lesson addTeacher(Lesson lesson, User teacher);
    Lesson removeTeacher(Lesson lesson, User teacher);
    Lesson replaceTeacher(Lesson lesson, User oldTeacher, User newTeacher);

    Lesson findById(Long id);
    List<Lesson> findAll();
    List<Lesson> findByCourt(Court court);
    List<Lesson> findByStartTime(LocalDateTime startTime);
    List<Lesson> findByEndTime(LocalDateTime endTime);
    List<Lesson> findByTimeInterval(LocalDateTime from, LocalDateTime to);
    List<Lesson> findByCapacity(Integer capacity);
    List<Lesson> findByLevel(Level level);
}
