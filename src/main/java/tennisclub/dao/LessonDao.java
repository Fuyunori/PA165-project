package tennisclub.dao;

import tennisclub.entity.Lesson;

import java.util.List;

/**
 * DAO interface for CRUDing Lessons, therefore:
 * - creating new Lessons
 * - updating Lessons
 * - removing Lessons
 * - retrieving all Lessons.
 *
 * Additionally, the interface allows the client to:
 * - find Lessons by ID,
 * - find Lessons by capacity.
 *
 * @author Xuan Linh Phamová
 */
public interface LessonDao {
    /**
     * Creates a new lesson.
     * @param lesson to be created
     */
    void create(Lesson lesson);

    /**
     * Updates a lesson.
     * @param lesson to be updated
     */
    void update(Lesson lesson);

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
}
