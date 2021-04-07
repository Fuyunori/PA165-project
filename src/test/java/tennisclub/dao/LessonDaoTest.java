package tennisclub.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.entity.enums.Level;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Miroslav Demek
 */
@SpringBootTest
@Transactional
public class LessonDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private LessonDao lessonDao;

    private Court court;

    private User teacher;
    private User student;

    private Lesson lesson;

    @BeforeEach
    public void before() {
        court = new Court();
        court.setName("Pretty nice court");
        court.setAddress("Brno");
        em.persist(court);

        teacher = new User();
        teacher.setUsername("mike");
        em.persist(teacher);

        student = new User();
        student.setUsername("jim");
        em.persist(student);

        lesson = new Lesson(LocalDateTime.of(2021, 4, 6, 13, 0 ),
                LocalDateTime.of(2021, 4, 6, 14, 0 ), Level.ADVANCED);
        lesson.setCapacity(10);
        lesson.setCourt(court);
        lesson.addTeacher(teacher);
        lesson.addStudent(student);
        em.persist(lesson);
    }


    @Test
    public void testCreateLesson() {
        Lesson created = new Lesson(LocalDateTime.of(2021, 4, 6, 13, 0 ),
                LocalDateTime.of(2021, 4, 6, 14, 0 ), Level.ADVANCED);
        lesson.setCapacity(10);
        created.setCourt(court);
        created.addStudent(student);
        created.addTeacher(teacher);

        lessonDao.create(created);

        Lesson found = em.find(Lesson.class, created.getId());
        assertThat(found).usingRecursiveComparison().isEqualTo(created);
    }

    @Test
    public void testCreateLessonWithNullCourt() {
        Lesson created = new Lesson(LocalDateTime.of(2021, 4, 6, 13, 0 ),
                LocalDateTime.of(2021, 4, 6, 14, 0 ), Level.ADVANCED);
        lesson.setCapacity(10);
        created.setCourt(null);
        created.addStudent(student);
        created.addTeacher(teacher);

        assertThatThrownBy(() -> lessonDao.create(created)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testCreateLessonWithNullStartTime() {
        Lesson created = new Lesson(null,
                LocalDateTime.of(2021, 4, 6, 14, 0 ), Level.ADVANCED);
        lesson.setCapacity(10);
        created.setCourt(court);
        created.addStudent(student);
        created.addTeacher(teacher);

        assertThatThrownBy(() -> lessonDao.create(created)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testCreateLessonWithNullEndTime() {
        Lesson created = new Lesson(LocalDateTime.of(2021, 4, 6, 13, 0 ),
                null, Level.ADVANCED);
        lesson.setCapacity(10);
        created.setCourt(court);
        created.addStudent(student);
        created.addTeacher(teacher);

        assertThatThrownBy(() -> lessonDao.create(created)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testCreateLessonWithNullLevel() {
        Lesson created = new Lesson(LocalDateTime.of(2021, 4, 6, 13, 0 ),
                LocalDateTime.of(2021, 4, 6, 14, 0 ), null);
        lesson.setCapacity(10);
        created.setCourt(court);
        created.addStudent(student);
        created.addTeacher(teacher);

        assertThatThrownBy(() -> lessonDao.create(created)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testCreateLessonWithStartTimeAfterEndTime() {
        Lesson created = new Lesson(LocalDateTime.of(2021, 4, 6, 19, 0 ),
                LocalDateTime.of(2021, 4, 6, 14, 0 ), Level.BEGINNER);
        lesson.setCapacity(10);
        created.setCourt(court);
        created.addStudent(student);
        created.addTeacher(teacher);

        assertThatThrownBy(() -> lessonDao.create(created)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testUpdate() {
        Court newCourt = new Court();
        newCourt.setAddress("Praha");
        newCourt.setName("Not so nice court");
        em.persist(newCourt);

        User newTeacher = new User();
        newTeacher.setUsername("new teacher");
        em.persist(newTeacher);

        lesson.setCourt(newCourt);
        lesson.setStartTime(LocalDateTime.of(2021, 4, 6, 12, 0 ));
        lesson.setStartTime(LocalDateTime.of(2021, 4, 6, 13, 0 ));
        lesson.setCapacity(20);
        lesson.setLevel(Level.BEGINNER);
        lesson.addTeacher(newTeacher);
        lesson.removeStudent(student);

        lesson = lessonDao.update(lesson);

        Lesson found = em.find(Lesson.class, lesson.getId());

        assertThat(found).usingRecursiveComparison().isEqualTo(lesson);
    }

    @Test
    public void testRemove() {
        lessonDao.remove(lesson);
        List<Lesson> found = em.createQuery("select l from Lesson l", Lesson.class).getResultList();
        assertThat(lesson).isNotIn(found);
    }

    @Test
    public void testRemoveAfterDetach() {
        em.detach(lesson);
        lessonDao.remove(lesson);
        Lesson deletedLesson = em.find(Lesson.class, lesson.getId());
        assertThat(deletedLesson).isEqualTo(null);
    }

    @Test
    public void testFindAll() {
        Lesson anotherLesson = new Lesson(LocalDateTime.of(2021, 3, 6, 22, 30 ),
                LocalDateTime.of(2021, 3, 6, 23, 0 ), Level.INTERMEDIATE);
        anotherLesson.setCapacity(5);
        anotherLesson.setCourt(court);
        em.persist(anotherLesson);

        List<Lesson> found = lessonDao.findAll();

        assertThat(found.size()).isEqualTo(2);
        assertThat(found).contains(lesson);
        assertThat(found).contains(anotherLesson);
    }

    @Test
    public void testFindById() {
        Lesson found = lessonDao.findById(lesson.getId());
        assertThat(found).usingRecursiveComparison().isEqualTo(lesson);
    }

    @Test
    public void testFindByCourt() {
        List<Lesson> found = lessonDao.findByCourt(lesson.getCourt());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void testFindByStartTime() {
        List<Lesson> found = lessonDao.findByStartTime(lesson.getStartTime());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void testFindByEndTime() {
        List<Lesson> found = lessonDao.findByEndTime(lesson.getEndTime());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void testFindByCapacity() {
        List<Lesson> found = lessonDao.findByCapacity(lesson.getCapacity());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void testFindByLevel() {
        List<Lesson> found = lessonDao.findByLevel(lesson.getLevel());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void testFindByIntervalContainedInLesson() {
        Lesson expected = createLessonFromTime(
                LocalDateTime.of(2022, 10, 5, 12, 30),
                LocalDateTime.of(2022, 10, 5, 14, 30)
        );
        em.persist(expected);

        List<Lesson> found = lessonDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 13, 0),
                LocalDateTime.of(2022, 10, 5, 14, 0)
        );

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(expected);
    }

    @Test
    public void testFindLessonContainedInInterval() {
        Lesson expected = createLessonFromTime(
                LocalDateTime.of(2022, 10, 5, 13, 0),
                LocalDateTime.of(2022, 10, 5, 14, 0)
        );
        em.persist(expected);

        List<Lesson> found = lessonDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 12, 0),
                LocalDateTime.of(2022, 10, 5, 15, 0)
        );

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(expected);
    }

    @Test
    public void testFindLessonIntersectingIntervalFromLeft() {
        Lesson expected = createLessonFromTime(
                LocalDateTime.of(2022, 10, 5, 13, 0),
                LocalDateTime.of(2022, 10, 5, 14, 30)
        );
        em.persist(expected);

        List<Lesson> found = lessonDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 14, 0),
                LocalDateTime.of(2022, 10, 5, 15, 0)
        );

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(expected);
    }

    @Test
    public void testFindLessonIntersectingIntervalFromRight() {
        Lesson expected = createLessonFromTime(
                LocalDateTime.of(2022, 10, 5, 14, 30),
                LocalDateTime.of(2022, 10, 5, 16, 0)
        );
        em.persist(expected);

        List<Lesson> found = lessonDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 14, 0),
                LocalDateTime.of(2022, 10, 5, 15, 0)
        );

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(expected);
    }

    @Test
    public void testFindByIntervalExclusivity() {
        Lesson expected = createLessonFromTime(
                LocalDateTime.of(2022, 10, 5, 14, 0),
                LocalDateTime.of(2022, 10, 5, 16, 0)
        );
        em.persist(expected);

        List<Lesson> foundLeft = lessonDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 13, 0),
                LocalDateTime.of(2022, 10, 5, 14, 0)
        );

        List<Lesson> foundRight = lessonDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 16, 0),
                LocalDateTime.of(2022, 10, 5, 17, 0)
        );

        assertThat(foundLeft).isEmpty();
        assertThat(foundRight).isEmpty();
    }


    private Lesson createLessonFromTime(LocalDateTime start, LocalDateTime end) {
        Lesson lesson = new Lesson(start, end, Level.ADVANCED);
        lesson.setCapacity(10);
        lesson.setCourt(court);
        lesson.addTeacher(teacher);
        lesson.addStudent(student);
        return lesson;
    }
}
