package tennisclub.facade;

import tennisclub.dto.lesson.LessonCreateDTO;
import tennisclub.dto.lesson.LessonFullDTO;
import tennisclub.dto.lesson.LessonWithCourtDTO;
import tennisclub.enums.Level;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Facade for manipulation with lessons
 * @author Xuan Linh Phamová
 */
public interface LessonFacade {

    /**
     * Creates a new lesson
     * @param lessonDTO containing data for lesson creation
     * @return full lesson dto
     */
    LessonFullDTO createLesson(LessonCreateDTO lessonDTO);

    /**
     * Deletes a lesson
     * @param lessonId of the lesson to be deleted
     */
    void deleteLesson(Long lessonId);

    /**
     * Enrolls a student into a lesson
     * @param lessonId of the lesson
     * @param studentId of the student
     */
    LessonFullDTO enrollStudent(Long lessonId, Long studentId);

    /**
     * Adds a teacher to a lesson
     * @param lessonId of the lesson
     * @param teacherId of the teacher
     */
    LessonFullDTO addTeacher(Long lessonId, Long teacherId);

    /**
     * Removes a student from a lesson
     * @param lessonId of the lesson
     * @param studentId of the student
     */
    LessonFullDTO withdrawStudent(Long lessonId, Long studentId);

    /**
     * Removes a teacher from a lesson
     * @param lessonId of the lesson
     * @param teacherId of the teacher
     */
    LessonFullDTO removeTeacher(Long lessonId, Long teacherId);

    /**
     * Finds a lesson based on the id
     * @param id of the lesson
     * @return the lesson DTO
     */
    LessonFullDTO getLessonWithId(Long id);

    /**
     * Finds all lessons
     * @return List of the lesson DTOs
     */
    List<LessonFullDTO> getAllLessons();

    /**
     * Finds lessons based on a start time
     * @param startTime of the lessons
     * @return list of the lesson DTOs
     */
    List<LessonFullDTO> getLessonsByStartTime(LocalDateTime startTime);

    /**
     * Finds lessons based on an end time
     * @param endTime of the lessons
     * @return list of the lesson DTOs
     */
    List<LessonFullDTO> getLessonsByEndTime(LocalDateTime endTime);

    /**
     * Finds lessons based on a level
     * @param level of the lessons
     * @return list of the lesson DTOs
     */
    List<LessonFullDTO> getLessonsByLevel(Level level);

    /**
     * Finds lessons based on a court
     * @param courtId of the court
     * @return list of the lesson DTOs
     */
    List<LessonFullDTO> getLessonsByCourt(Long courtId);
}
