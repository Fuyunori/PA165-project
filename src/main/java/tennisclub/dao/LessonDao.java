package tennisclub.dao;

import tennisclub.entity.Lesson;
import tennisclub.entity.enums.Level;

import java.util.List;

/**
 * DAO interface for CRUD operations on Lesson.
 *
 * @author Xuan Linh Phamová
 */
public interface LessonDao {
    /**
     * Persists a new lesson.
     * @param lesson to be created
     */
    void create(Lesson lesson);

    /**
     * Updates a lesson.
     * @param lesson to be updated
     */
    Lesson update(Lesson lesson);

    /**
     * Removes a lesson. In case the lesson is in detached state,
     * it is reattached and then removed.
     * @param lesson to be removed
     */
    void remove(Lesson lesson);

    /**
     * Retrieves all Lessons.
     * @return all lessons
     */
    List<Lesson> findAll();

    /**
     * Finds a particular Lesson by its id.
     * @param id of the searched lesson
     * @return a Lesson whose id matches the given id
     */
    Lesson findById(Long id);

    //List<Lesson> findByLecturerName(String lecturerName);

    /**
     * Finds all Lessons with particular capacity.
     * @param capacity of the lessons
     * @return all Lessons which have the given capacity
     */
    List<Lesson> findByCapacity(Integer capacity);

    /**
     * Finds all lessons with a particular level.
     * @param level of the lessons
     * @return all lesson which have the given level
     */
    List<Lesson> findByLevel(Level level);
}
