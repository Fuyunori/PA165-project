package tennisclub.facade;

import tennisclub.dto.lesson.LessonCreateDTO;
import tennisclub.dto.lesson.LessonWithCourtDTO;
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

    LessonWithCourtDTO getLessonWithId(Long id);
    List<LessonWithCourtDTO> getAllLessons();
    List<LessonWithCourtDTO> getLessonsByStartTime(LocalDateTime startTime);
    List<LessonWithCourtDTO> getLessonsByEndTime(LocalDateTime endTime);
    List<LessonWithCourtDTO> getLessonsByLevel(Level level);
    List<LessonWithCourtDTO> getLessonsByCourt(Long courtId);
}
