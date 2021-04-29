package tennisclub.facade;

import tennisclub.dto.lesson.LessonCreateDTO;
import tennisclub.dto.lesson.LessonDTO;
import tennisclub.enums.Level;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonFacade {
    Long createLesson(LessonCreateDTO lessonDTO);
    void deleteLesson(Long lessonId);

    void enrollStudent(Long lessonId, Long studentId);
    void addTeacher(Long lessonId, Long teacherId);

    void withdrawStudent(Long lessonId, Long studentId);
    void removeTeacher(Long lessonId, Long teacherId);

    LessonDTO getLessonWithId(Long id);
    List<LessonDTO> getAllLessons();
    List<LessonDTO> getLessonsByStartTime(LocalDateTime startTime);
    List<LessonDTO> getLessonsByEndTime(LocalDateTime endTime);
    List<LessonDTO> getLessonsByLevel(Level level);
    List<LessonDTO> getLessonsByCourt(Long courtId);
}
