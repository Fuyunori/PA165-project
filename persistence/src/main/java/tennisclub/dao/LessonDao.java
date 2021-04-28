package tennisclub.dao;

import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.enums.Level;

import java.time.LocalDateTime;
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
     * @return updated lesson
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
     * Finds all Lessons taking place on the specified court.
     * @param court on which the Lessons take place
     * @return all Lessons taking place on the given court
     */
    List<Lesson> findByCourt(Court court);

    /**
     * Finds all Lessons starting at the specified time.
     * @param startTime at which the Lessons start
     * @return all Lessons starting at the given time
     */
    List<Lesson> findByStartTime(LocalDateTime startTime);

    /**
     * Finds all Lessons ending at the specified time.
     * @param endTime at which the Lessons end
     * @return all Lessons ending at the given time
     */
    List<Lesson> findByEndTime(LocalDateTime endTime);

    /**
     * Finds all Lessons that at least partially take place
     * during the specified time interval. The interval is exclusive.
     *
     * More formally, retrieve all Lessons l such that:
     *     l.startTime < to && l.endTime > from
     *
     * The behaviour of this method is undefined if:
     *     from > to
     *
     * @param from the beginning of the interval
     * @param to the end of the interval
     * @return all Lessons whose time falls into the interval
     */
    List<Lesson> findByTimeInterval(LocalDateTime from, LocalDateTime to);

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
