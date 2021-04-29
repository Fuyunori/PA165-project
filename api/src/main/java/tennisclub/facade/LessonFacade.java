package tennisclub.facade;

import tennisclub.dto.lesson.LessonCreateDTO;
import tennisclub.dto.lesson.LessonDTO;
import tennisclub.dto.lesson.LessonFullDTO;
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

    LessonFullDTO getLessonWithId(Long id);
    List<LessonFullDTO> getAllLessons();
    List<LessonFullDTO> getLessonsByStartTime(LocalDateTime startTime);
    List<LessonFullDTO> getLessonsByEndTime(LocalDateTime endTime);
    List<LessonFullDTO> getLessonsByLevel(Level level);
    List<LessonFullDTO> getLessonsByCourt(Long courtId);
}
