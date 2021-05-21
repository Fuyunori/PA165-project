package tennisclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import tennisclub.ServiceTestsConfiguration;
import tennisclub.dao.LessonDao;
import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.enums.Level;
import tennisclub.exceptions.ServiceLayerException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Xuan Linh Phamová
 */
@SpringBootTest
@ContextConfiguration(classes = ServiceTestsConfiguration.class)
public class LessonServiceTest {

    @MockBean
    private LessonDao lessonDao;

    @MockBean
    private TimeService timeService;

    @Autowired
    private LessonService lessonService;

    private Lesson lesson;
    private User student;
    private User newStudent;
    private User teacher;
    private User newTeacher;

    @BeforeEach
    public void setup() {
        lesson = makeLesson(makeTime(13), makeTime(14), Level.BEGINNER, 17);
        lesson.setId(1L);

        student = makeUser("Lukash", "luke", "luk@buk.com");
        newStudent = makeUser("Joe", "dzou", "ej@oj.com");
        teacher = makeUser("aaa", "bbb", "ccc@ddd.com");
        newTeacher = makeUser("eee", "fff", "ggg@hhh.com");

        student.addLessonToAttend(lesson);
        teacher.addLessonToTeach(lesson);
    }

    @Test
    public void createLessonTest() {
        lessonService.create(lesson);
        verify(lessonDao).create(lesson);
    }

    @Test
    public void updateLessonTest() {
        lesson.setCapacity(12);
        lesson.setLevel(Level.ADVANCED);
        lesson.setStartTime(LocalDateTime.of(2021, 4, 25, 9, 0));

        when(lessonDao.update(lesson)).thenReturn(lesson);

        Lesson actual = lessonService.update(lesson);

        verify(lessonDao).update(lesson);
        assertThat(actual).isEqualTo(lesson);
    }

    @Test
    public void deleteLessonTest() {
        lessonService.remove(lesson);
        verify(lessonDao).remove(lesson);
    }

    @Test
    public void enrollStudentTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().minusDays(1));
        when(lessonDao.update(lesson)).thenReturn(lesson);

        Lesson updated = lessonService.enrollStudent(lesson, newStudent);

        verify(lessonDao).update(lesson);
        assertThat(updated.getStudents()).contains(newStudent);
        assertThat(newStudent.getLessonsToAttend()).contains(lesson);
    }

    @Test
    public void enrollAlreadyEnrolledStudentTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().minusDays(1));

        assertThatThrownBy(() -> lessonService.enrollStudent(lesson, student))
                .isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void enrollStudentAfterLessonStartTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().plusDays(1));

        assertThatThrownBy(() -> lessonService.enrollStudent(lesson, newStudent))
                .isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void enrollStudentFullCapacityTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().minusDays(1));
        lesson.setCapacity(lesson.getStudents().size());

        assertThatThrownBy(() -> lessonService.enrollStudent(lesson, newStudent))
                .isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void withdrawStudentTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().minusDays(1));
        when(lessonDao.update(lesson)).thenReturn(lesson);

        Lesson updated = lessonService.withdrawStudent(lesson, student);

        verify(lessonDao).update(lesson);
        assertThat(updated.getStudents()).doesNotContain(student);
        assertThat(student.getLessonsToAttend()).doesNotContain(lesson);
    }

    @Test
    public void withdrawNonEnrolledStudentTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().minusDays(1));

        assertThatThrownBy(() -> lessonService.withdrawStudent(lesson, newStudent))
                .isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void withdrawStudentAfterStartTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().plusDays(1));

        assertThatThrownBy(() -> lessonService.withdrawStudent(lesson, student))
                .isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void addTeacherTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().minusDays(1));
        when(lessonDao.update(lesson)).thenReturn(lesson);

        Lesson updated = lessonService.addTeacher(lesson, newTeacher);

        verify(lessonDao).update(lesson);
        assertThat(updated.getTeachers()).contains(newTeacher);
        assertThat(newTeacher.getLessonsToTeach()).contains(lesson);
    }

    @Test
    public void addAlreadyTeachingTeacherTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().minusDays(1));

        assertThatThrownBy(() -> lessonService.addTeacher(lesson, teacher))
                .isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void addTeacherAfterStartTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().plusDays(1));

        assertThatThrownBy(() -> lessonService.addTeacher(lesson, newTeacher))
                .isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void removeTeacherTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().minusDays(1));
        when(lessonDao.update(lesson)).thenReturn(lesson);

        Lesson updated = lessonService.removeTeacher(lesson, teacher);

        verify(lessonDao).update(lesson);
        assertThat(updated.getTeachers()).doesNotContain(teacher);
        assertThat(teacher.getLessonsToTeach()).doesNotContain(lesson);
    }

    @Test
    public void removeTeacherAfterStartTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().plusDays(1));

        assertThatThrownBy(() -> lessonService.removeTeacher(lesson, teacher))
                .isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void removeNonTeachingTeacherTest() {
        when(timeService.getCurrentDateTime()).thenReturn(lesson.getStartTime().minusDays(1));

        assertThatThrownBy(() -> lessonService.removeTeacher(lesson, newTeacher))
                .isInstanceOf(ServiceLayerException.class);
    }

    @Test
    public void findByIdTest() {
        when(lessonDao.findById(lesson.getId())).thenReturn(lesson);

        Lesson found = lessonService.findById(lesson.getId());

        verify(lessonDao).findById(lesson.getId());
        assertThat(found).isEqualTo(lesson);
    }

    @Test
    public void findByNonExistingIdTest() {
        when(lessonDao.findById(10L)).thenReturn(null);

        Lesson found = lessonService.findById(10L);

        verify(lessonDao).findById(10L);
        assertThat(found).isNull();
    }

    @Test
    public void findAllTest() {
        Lesson lesson1 = makeLesson(makeTime(10), makeTime(12), Level.BEGINNER, 25);
        Lesson lesson2 = makeLesson(makeTime(13), makeTime(14), Level.ADVANCED, 20);
        Lesson lesson3 = makeLesson(makeTime(20), makeTime(21), Level.INTERMEDIATE, 3);
        List<Lesson> lessons = asList(lesson1, lesson2, lesson3);
        when(lessonDao.findAll()).thenReturn(lessons);

        List<Lesson> found = lessonService.findAll();

        verify(lessonDao).findAll();
        assertThat(found.size()).isEqualTo(3);
        assertThat(found).contains(lesson1);
        assertThat(found).contains(lesson2);
        assertThat(found).contains(lesson3);
    }

    @Test
    public void findAllEmptyTest() {
        when(lessonDao.findAll()).thenReturn(Collections.emptyList());

        List<Lesson> found = lessonService.findAll();

        assertThat(found).isEmpty();
    }

    @Test
    public void findByCourtTest() {
        Court court = new Court();
        lesson.setCourt(court);
        when(lessonDao.findByCourt(court)).thenReturn(Collections.singletonList(lesson));

        List<Lesson> found = lessonService.findByCourt(court);

        verify(lessonDao).findByCourt(court);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void findByCourtEmptyTest() {
        Court court = new Court();
        when(lessonDao.findByCourt(court)).thenReturn(Collections.emptyList());

        List<Lesson> found = lessonService.findByCourt(court);

        verify(lessonDao).findByCourt(court);
        assertThat(found).isEmpty();
    }

    @Test
    public void findByStartTimeTest() {
        when(lessonDao.findByStartTime(lesson.getStartTime()))
                .thenReturn(Collections.singletonList(lesson));

        List<Lesson> found = lessonService.findByStartTime(lesson.getStartTime());

        verify(lessonDao).findByStartTime(lesson.getStartTime());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void findByStartTimeEmptyTest() {
        when(lessonDao.findByStartTime(lesson.getStartTime()))
                .thenReturn(Collections.emptyList());

        List<Lesson> found = lessonService.findByStartTime(lesson.getStartTime());

        verify(lessonDao).findByStartTime(lesson.getStartTime());
        assertThat(found).isEmpty();
    }

    @Test
    public void findByEndTimeTest() {
        when(lessonDao.findByEndTime(lesson.getEndTime()))
                .thenReturn(Collections.singletonList(lesson));

        List<Lesson> found = lessonService.findByEndTime(lesson.getEndTime());

        verify(lessonDao).findByEndTime(lesson.getEndTime());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void findByEndTimeEmptyTest() {
        when(lessonDao.findByEndTime(lesson.getEndTime()))
                .thenReturn(Collections.emptyList());

        List<Lesson> found = lessonService.findByEndTime(lesson.getEndTime());

        verify(lessonDao).findByEndTime(lesson.getEndTime());
        assertThat(found).isEmpty();
    }

    @Test
    public void findByTimeIntervalTest() {
        Lesson lesson1 = makeLesson(makeTime(10), makeTime(12), Level.BEGINNER, 25);
        Lesson lesson2 = makeLesson(makeTime(13), makeTime(14), Level.ADVANCED, 20);
        List<Lesson> lessons = asList(lesson1, lesson2);
        LocalDateTime from = makeTime(10);
        LocalDateTime to = makeTime(14);
        when(lessonDao.findByTimeInterval(from ,to)).thenReturn(lessons);

        List<Lesson> found = lessonService.findByTimeInterval(from ,to);

        verify(lessonDao).findByTimeInterval(from ,to);
        assertThat(found.size()).isEqualTo(2);
        assertThat(found).contains(lesson1);
        assertThat(found).contains(lesson2);
    }

    @Test
    public void findByTimeIntervalEmptyTest() {
        LocalDateTime from = makeTime(10);
        LocalDateTime to = makeTime(14);
        when(lessonDao.findByTimeInterval(from ,to)).thenReturn(Collections.emptyList());

        List<Lesson> found = lessonService.findByTimeInterval(from ,to);

        verify(lessonDao).findByTimeInterval(from ,to);
        assertThat(found).isEmpty();
    }

    @Test
    public void findByCapacityTest() {
        when(lessonDao.findByCapacity(lesson.getCapacity())).thenReturn(Collections.singletonList(lesson));

        List<Lesson> found = lessonService.findByCapacity(lesson.getCapacity());

        verify(lessonDao).findByCapacity(lesson.getCapacity());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void findByCapacityEmptyTest() {
        when(lessonDao.findByCapacity(42)).thenReturn(Collections.emptyList());

        List<Lesson> found = lessonService.findByCapacity(lesson.getCapacity());

        verify(lessonDao).findByCapacity(lesson.getCapacity());
        assertThat(found).isEmpty();
    }

    @Test
    public void findByLevelTest() {
        when(lessonDao.findByLevel(lesson.getLevel())).thenReturn(Collections.singletonList(lesson));

        List<Lesson> found = lessonService.findByLevel(lesson.getLevel());

        verify(lessonDao).findByLevel(lesson.getLevel());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void findByLevelEmptyTest() {
        when(lessonDao.findByLevel(Level.ADVANCED)).thenReturn(Collections.emptyList());

        List<Lesson> found = lessonService.findByLevel(lesson.getLevel());

        verify(lessonDao).findByLevel(lesson.getLevel());
        assertThat(found).isEmpty();
    }


    private User makeUser(String name, String userName, String email) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(userName);
        user.setName(name);
        return user;
    }

    private Lesson makeLesson(LocalDateTime start, LocalDateTime end, Level level, Integer capacity) {
        Lesson lesson = new Lesson(start, end, level);
        lesson.setCapacity(capacity);
        return lesson;
    }

    private LocalDateTime makeTime(int hours) {
        return LocalDateTime.of(2021, 4, 20, hours, 0);
    }
}
