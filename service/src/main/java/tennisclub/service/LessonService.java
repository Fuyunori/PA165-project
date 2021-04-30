package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.enums.Level;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Service for manipulation with lessons
 * @author Xuan Linh Phamová
 */
@Service
public interface LessonService {

    /**
     * Creates a new lesson
     * @param lesson to be created
     * @return the created lesson
     */
    Lesson create(Lesson lesson);

    /**
     * Updates a lesson
     * @param lesson to be updated
     * @return the updated lesson
     */
    Lesson update(Lesson lesson);

    /**
     * Deletes a lesson
     * @param lesson to be deleted
     */
    void remove(Lesson lesson);

    /**
     * Enrolls a student into a lesson
     * @param lesson to which the student will be added
     * @param student to be enrolled
     * @return the updated lesson
     */
    Lesson enrollStudent(Lesson lesson, User student);

    /**
     * Removes a student from a lesson
     * @param lesson from which the student will be removed
     * @param student to be removed
     * @return the updated lesson
     */
    Lesson withdrawStudent(Lesson lesson, User student);

    /**
     * Adds a teacher into a lesson
     * @param lesson to which the teacher will be added
     * @param teacher to be added
     * @return the updated lesson
     */
    Lesson addTeacher(Lesson lesson, User teacher);

    /**
     * Removes a teacher from a lesson
     * @param lesson from which the student will be removed
     * @param teacher to be removed
     * @return the updated lesson
     */
    Lesson removeTeacher(Lesson lesson, User teacher);

    /**
     * Replaces a teacher in a lesson
     * @param lesson in which the teacher will be replaced
     * @param oldTeacher to be replaced
     * @param newTeacher that will replace the old teacher
     * @return the updated lesson
     */
    Lesson replaceTeacher(Lesson lesson, User oldTeacher, User newTeacher);

    /**
     * Finds a lesson based on its id
     * @param id of the lesson
     * @return the found lesson
     */
    Lesson findById(Long id);

    /**
     * Finds all lessons in the system
     * @return all the lessons
     */
    List<Lesson> findAll();

    /**
     * Finds lessons based on their court
     * @param court of the lesson
     * @return lessons with a given court
     */
    List<Lesson> findByCourt(Court court);

    /**
     * Finds lessons based on their start time
     * @param startTime of the lesson
     * @return lessons with a given start time
     */
    List<Lesson> findByStartTime(LocalDateTime startTime);

    /**
     * Finds lessons based on their end time
     * @param endTime of the lesson
     * @return lessons with a given end time
     */
    List<Lesson> findByEndTime(LocalDateTime endTime);

    /**
     * Finds lessons within an interval
     * @param from the start of the interval
     * @param to the end of the interval
     * @return lessons within the interval
     */
    List<Lesson> findByTimeInterval(LocalDateTime from, LocalDateTime to);

    /**
     * Finds lessons based on their capacity
     * @param capacity of the lesson
     * @return lessons with the exact capacity
     */
    List<Lesson> findByCapacity(Integer capacity);

    /**
     * Finds lessons based on their difficulty level
     * @param level difficulty level of the lesson
     * @return lessons with a given difficulty level
     */
    List<Lesson> findByLevel(Level level);
}
