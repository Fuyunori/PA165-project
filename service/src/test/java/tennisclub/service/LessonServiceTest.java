package tennisclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dao.LessonDao;
import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.enums.Level;
import tennisclub.exceptions.TennisClubManagerException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LessonServiceTest {

    @MockBean
    private LessonDao lessonDao;

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

        lesson.addStudent(student);
        lesson.addTeacher(teacher);
    }

    @Test
    public void createLesson() {
        lessonService.create(lesson);
        verify(lessonDao).create(lesson);
    }

    @Test
    public void updateLesson() {
        Lesson expected = lesson;
        expected.setCapacity(12);
        expected.setLevel(Level.ADVANCED);
        expected.setStartTime(LocalDateTime.of(2021, 4, 25, 9, 0));

        when(lessonDao.update(expected)).thenReturn(expected);

        Lesson actual = lessonService.update(lesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void deleteLesson() {
        lessonService.remove(lesson);
        verify(lessonDao).remove(lesson);
    }

    @Test
    public void enrollStudent() {
        when(lessonDao.update(lesson)).thenReturn(lesson);

        Lesson updated = lessonService.enrollStudent(lesson, newStudent);

        verify(lessonDao).update(lesson);
        assertThat(updated.getStudents()).contains(newStudent);
    }

    @Test
    public void enrollAlreadyEnrolledStudent() {
        assertThatThrownBy(() -> lessonService.enrollStudent(lesson, student))
                .isInstanceOf(TennisClubManagerException.class);
    }

    @Test
    public void withdrawStudent() {
        when(lessonDao.update(lesson)).thenReturn(lesson);

        Lesson updated = lessonService.withdrawStudent(lesson, student);

        verify(lessonDao).update(lesson);
        assertThat(updated.getStudents()).doesNotContain(student);
    }

    @Test
    public void withdrawNonEnrolledStudent() {
        assertThatThrownBy(() -> lessonService.withdrawStudent(lesson, newStudent))
                .isInstanceOf(TennisClubManagerException.class);
    }

    @Test
    public void addTeacher() {
        when(lessonDao.update(lesson)).thenReturn(lesson);

        Lesson updated = lessonService.addTeacher(lesson, newTeacher);

        verify(lessonDao).update(lesson);
        assertThat(updated.getTeachers()).contains(newTeacher);
    }

    @Test
    public void addAlreadyTeachingTeacher() {
        assertThatThrownBy(() -> lessonService.addTeacher(lesson, teacher))
                .isInstanceOf(TennisClubManagerException.class);
    }

    @Test
    public void removeTeacher() {
        when(lessonDao.update(lesson)).thenReturn(lesson);

        Lesson updated = lessonService.removeTeacher(lesson, teacher);

        verify(lessonDao).update(lesson);
        assertThat(updated.getTeachers()).doesNotContain(teacher);
    }

    @Test
    public void removeNonTeachingTeacher() {
        assertThatThrownBy(() -> lessonService.removeTeacher(lesson, newTeacher))
                .isInstanceOf(TennisClubManagerException.class);
    }

    @Test
    public void findById() {
        when(lessonDao.findById(lesson.getId())).thenReturn(lesson);

        Lesson found = lessonService.findById(lesson.getId());

        assertThat(found).isEqualTo(lesson);
    }

    @Test
    public void findAllTest() {
        Lesson lesson1 = makeLesson(makeTime(10), makeTime(12), Level.BEGINNER, 25);
        Lesson lesson2 = makeLesson(makeTime(13), makeTime(14), Level.ADVANCED, 20);
        Lesson lesson3 = makeLesson(makeTime(20), makeTime(21), Level.INTERMEDIATE, 3);
        List<Lesson> lessons = asList(lesson1, lesson2, lesson3);
        when(lessonDao.findAll()).thenReturn(lessons);

        List<Lesson> found = lessonService.findAll();

        assertThat(found.size()).isEqualTo(3);
        assertThat(found).contains(lesson1);
        assertThat(found).contains(lesson2);
        assertThat(found).contains(lesson3);
    }

    @Test
    public void findByCourtTest() {
        Court court = new Court();
        lesson.setCourt(court);
        when(lessonDao.findByCourt(court)).thenReturn(Collections.singletonList(lesson));

        List<Lesson> found = lessonService.findByCourt(court);

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void findByStartTimeTest() {
        when(lessonDao.findByStartTime(lesson.getStartTime()))
                .thenReturn(Collections.singletonList(lesson));

        List<Lesson> found = lessonService.findByStartTime(lesson.getStartTime());

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void findByStartTimeEmptyTest() {
        when(lessonDao.findByStartTime(lesson.getStartTime()))
                .thenReturn(Collections.emptyList());

        List<Lesson> found = lessonService.findByStartTime(lesson.getStartTime());

        assertThat(found).isEmpty();
    }

    @Test
    public void findByEndTimeTest() {
        when(lessonDao.findByEndTime(lesson.getEndTime()))
                .thenReturn(Collections.singletonList(lesson));

        List<Lesson> found = lessonService.findByEndTime(lesson.getEndTime());

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void findByEndTimeEmptyTest() {
        when(lessonDao.findByStartTime(lesson.getEndTime()))
                .thenReturn(Collections.emptyList());

        List<Lesson> found = lessonService.findByEndTime(lesson.getStartTime());

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

        assertThat(found.size()).isEqualTo(2);
        assertThat(found).contains(lesson1);
        assertThat(found).contains(lesson2);
    }

    @Test
    public void findByCapacityTest() {
        when(lessonDao.findByCapacity(lesson.getCapacity())).thenReturn(Collections.singletonList(lesson));

        List<Lesson> found = lessonService.findByCapacity(lesson.getCapacity());

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
    }

    @Test
    public void findByLevelTest() {
        when(lessonDao.findByLevel(lesson.getLevel())).thenReturn(Collections.singletonList(lesson));

        List<Lesson> found = lessonService.findByLevel(lesson.getLevel());

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lesson);
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
