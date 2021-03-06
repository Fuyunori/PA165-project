package tennisclub.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import tennisclub.ServiceTestsConfiguration;
import tennisclub.dto.court.CourtDto;
import tennisclub.dto.lesson.LessonCreateDTO;
import tennisclub.dto.lesson.LessonFullDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.enums.Level;
import tennisclub.exceptions.FacadeLayerException;
import tennisclub.service.CourtService;
import tennisclub.service.LessonService;
import tennisclub.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Xuan Linh Phamová
 */
@SpringBootTest
@ContextConfiguration(classes = ServiceTestsConfiguration.class)
public class LessonFacadeTest {

    @MockBean
    private LessonService lessonService;
    @MockBean
    private CourtService courtService;
    @MockBean
    private UserService userService;

    @Autowired
    private  LessonFacade lessonFacade;

    private Court court;
    private CourtDto courtDto;
    private User user;
    private Lesson lesson;
    private LessonFullDTO lessonDTO;
    private UserDTO userDTO;

    @BeforeEach
    public void setup() {
        courtDto = makeCourtDTO("name", "address");
        court = makeCourt(courtDto.getName(), courtDto.getAddress());
        user = makeUser("Joe", "Bob", "john@neco.com");
        user.setId(5L);

        lesson = new Lesson(makeTime(14), makeTime(15), Level.ADVANCED);
        lesson.setCapacity(25);
        lesson.setId(7L);
        lesson.setCourt(court);

        lessonDTO = new LessonFullDTO();
        lessonDTO.setCapacity(lesson.getCapacity());
        lessonDTO.setLevel(lesson.getLevel());
        lessonDTO.setStartTime(lesson.getStartTime());
        lessonDTO.setEndTime(lesson.getEndTime());
        lessonDTO.setCourt(courtDto);
        lessonDTO.setId(lesson.getId());
        lessonDTO.setStudents(new HashSet<>());
        lessonDTO.setTeachers(new HashSet<>());

        userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
    }


    @Test
    public void createTest() {
        LessonCreateDTO lessonCreateDTO = new LessonCreateDTO();
        lessonCreateDTO.setCapacity(lesson.getCapacity());
        lessonCreateDTO.setStartTime(lesson.getStartTime());
        lessonCreateDTO.setEndTime(lesson.getEndTime());
        lessonCreateDTO.setCourt(courtDto);

        when(courtService.isFree(court, lessonCreateDTO.getStartTime(), lessonCreateDTO.getEndTime()))
                .thenReturn(true);
        when(lessonService.create(lesson)).thenReturn(lesson);

        LessonFullDTO lessonDTO = lessonFacade.createLesson(lessonCreateDTO);

        verify(courtService).isFree(court, lessonCreateDTO.getStartTime(), lessonCreateDTO.getEndTime());
        verify(lessonService).create(lesson);
        assertThat(lessonDTO).isEqualTo(this.lessonDTO);
    }

    @Test
    public void createCourtNotFreeTest() {
        LessonCreateDTO lessonCreateDTO = new LessonCreateDTO();
        lessonCreateDTO.setCapacity(15);
        lessonCreateDTO.setStartTime(makeTime(10));
        lessonCreateDTO.setEndTime(makeTime(12));
        lessonCreateDTO.setCourt(courtDto);

        when(courtService.isFree(court, lessonCreateDTO.getStartTime(), lessonCreateDTO.getEndTime()))
                .thenReturn(false);
        when(lessonService.create(lesson)).thenReturn(lesson);

        assertThatThrownBy(() -> lessonFacade.createLesson(lessonCreateDTO))
                .isInstanceOf(FacadeLayerException.class);
    }

    @Test
    public void deleteLessonTest() {
        when(lessonService.findById(lesson.getId())).thenReturn(lesson);

        lessonFacade.deleteLesson(lesson.getId());

        verify(lessonService).findById(lesson.getId());
        verify(lessonService).remove(lesson);
    }

    @Test
    public void enrollStudentTest() {
        Lesson expected = lesson;
        expected.addStudent(user);

        LessonFullDTO expectedDto = lessonDTO;
        Set<UserDTO> students = expectedDto.getStudents();
        students.add(userDTO);
        expectedDto.setStudents(students);

        when(lessonService.findById(lesson.getId())).thenReturn(lesson);
        when(lessonService.enrollStudent(lesson, user)).thenReturn(expected);
        when(userService.findUserById(user.getId())).thenReturn(user);

        LessonFullDTO actual = lessonFacade.enrollStudent(lesson.getId(), user.getId());

        verify(lessonService).findById(lesson.getId());
        verify(userService).findUserById(user.getId());
        verify(lessonService).enrollStudent(lesson, user);
        assertThat(actual).isEqualTo(expectedDto);
    }

    @Test
    public void addTeacherTest() {
        Lesson expected = lesson;
        expected.addTeacher(user);

        LessonFullDTO expectedDto = lessonDTO;
        Set<UserDTO> teachers = expectedDto.getTeachers();
        teachers.add(userDTO);
        expectedDto.setTeachers(teachers);

        when(lessonService.findById(lesson.getId())).thenReturn(lesson);
        when(lessonService.addTeacher(lesson, user)).thenReturn(expected);
        when(userService.findUserById(user.getId())).thenReturn(user);

        LessonFullDTO actual = lessonFacade.addTeacher(lesson.getId(), user.getId());

        verify(lessonService).findById(lesson.getId());
        verify(userService).findUserById(user.getId());
        verify(lessonService).addTeacher(lesson, user);
        assertThat(actual).isEqualTo(expectedDto);
    }

    @Test
    public void withdrawStudentTest() {
        Lesson expected = lesson;
        expected.removeStudent(user);

        LessonFullDTO expectedDto = lessonDTO;
        Set<UserDTO> students = expectedDto.getStudents();
        students.remove(userDTO);
        expectedDto.setStudents(students);

        when(lessonService.findById(lesson.getId())).thenReturn(lesson);
        when(lessonService.withdrawStudent(lesson, user)).thenReturn(expected);
        when(userService.findUserById(user.getId())).thenReturn(user);

        LessonFullDTO actual = lessonFacade.withdrawStudent(lesson.getId(), user.getId());

        verify(lessonService).findById(lesson.getId());
        verify(userService).findUserById(user.getId());
        verify(lessonService).withdrawStudent(lesson, user);
        assertThat(actual).isEqualTo(expectedDto);
    }

    @Test
    public void removeTeacherTest() {
        Lesson expected = lesson;
        expected.removeTeacher(user);

        LessonFullDTO expectedDto = lessonDTO;
        Set<UserDTO> teachers = expectedDto.getTeachers();
        teachers.remove(userDTO);
        expectedDto.setTeachers(teachers);

        when(lessonService.findById(lesson.getId())).thenReturn(lesson);
        when(lessonService.removeTeacher(lesson, user)).thenReturn(expected);
        when(userService.findUserById(user.getId())).thenReturn(user);

        LessonFullDTO actual = lessonFacade.removeTeacher(lesson.getId(), user.getId());

        verify(lessonService).findById(lesson.getId());
        verify(userService).findUserById(user.getId());
        verify(lessonService).removeTeacher(lesson, user);
        assertThat(actual).isEqualTo(expectedDto);
    }

    @Test
    public void getLessonWithIdTest() {
        when(lessonService.findById(lesson.getId())).thenReturn(lesson);

        LessonFullDTO found = lessonFacade.getLessonWithId(lesson.getId());

        verify(lessonService).findById(lesson.getId());
        assertThat(found).isEqualTo(lessonDTO);
    }

    @Test
    public void getAllLessonsTest() {
        when(lessonService.findAll()).thenReturn(Collections.singletonList(lesson));

        List<LessonFullDTO> found = lessonFacade.getAllLessons();

        verify(lessonService).findAll();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lessonDTO);
    }

    @Test
    public void getLessonsByCourtTest() {
        when(courtService.getById(court.getId())).thenReturn(court);
        when(lessonService.findByCourt(court)).thenReturn(Collections.singletonList(lesson));

        List<LessonFullDTO> found = lessonFacade.getLessonsByCourt(court.getId());

        verify(courtService).getById(court.getId());
        verify(lessonService).findByCourt(court);
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lessonDTO);
    }

    @Test
    public void getLessonsByStartTimeTest() {
        when(lessonService.findByStartTime(lesson.getStartTime())).thenReturn(Collections.singletonList(lesson));

        List<LessonFullDTO> found = lessonFacade.getLessonsByStartTime(lesson.getStartTime());

        verify(lessonService).findByStartTime(lesson.getStartTime());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lessonDTO);
    }

    @Test
    public void getLessonsByEndTimeTest() {
        when(lessonService.findByEndTime(lesson.getEndTime())).thenReturn(Collections.singletonList(lesson));

        List<LessonFullDTO> found = lessonFacade.getLessonsByEndTime(lesson.getEndTime());

        verify(lessonService).findByEndTime(lesson.getEndTime());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lessonDTO);
    }

    @Test
    public void getLessonsByLevelTest() {
        when(lessonService.findByLevel(lesson.getLevel())).thenReturn(Collections.singletonList(lesson));

        List<LessonFullDTO> found = lessonFacade.getLessonsByLevel(lesson.getLevel());

        verify(lessonService).findByLevel(lesson.getLevel());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(lessonDTO);
    }

    private CourtDto makeCourtDTO(String name, String address) {
        CourtDto court = new CourtDto();
        court.setAddress(address);
        court.setName(name);
        return court;
    }

    private Court makeCourt(String name, String address) {
        Court court = new Court();
        court.setAddress(address);
        court.setName(name);
        return court;
    }

    private User makeUser(String name, String userName, String email) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(userName);
        user.setName(name);
        return user;
    }

    private LocalDateTime makeTime(int hours) {
        return LocalDateTime.of(2021, 4, 20, hours, 0);
    }
}
