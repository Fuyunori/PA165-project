package tennisclub.facade;

import org.dozer.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisclub.dto.lesson.LessonCreateDTO;
import tennisclub.dto.lesson.LessonDTO;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.enums.Level;
import tennisclub.service.CourtService;
import tennisclub.service.LessonService;
import tennisclub.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LessonFacadeImpl implements LessonFacade {
    private final Mapper mapper;
    private final LessonService lessonService;
    private final CourtService courtService;
    private final UserService userService;

    @Autowired
    public LessonFacadeImpl(Mapper mapper,
                            LessonService lessonService,
                            CourtService courtService,
                            UserService userService){
        this.mapper = mapper;
        this.lessonService = lessonService;
        this.courtService = courtService;
        this.userService = userService;
    }

    @Override
    public Long createLesson(LessonCreateDTO lessonDTO) {
        Lesson lesson = mapper.map(lessonDTO, Lesson.class);
        Lesson newLesson = lessonService.create(lesson);
        return newLesson.getId();
    }

    @Override
    public void deleteLesson(Long lessonId) {
        Lesson lesson = lessonService.findById(lessonId);
        lessonService.remove(lesson);
    }

    @Override
    public void enrollStudent(Long lessonId, Long studentId) {
        Lesson lesson = lessonService.findById(lessonId);
        User student = userService.findUserById(studentId);
        lessonService.enrollStudent(lesson, student);
    }

    @Override
    public void addTeacher(Long lessonId, Long teacherId) {
        Lesson lesson = lessonService.findById(lessonId);
        User teacher = userService.findUserById(teacherId);
        lessonService.addTeacher(lesson, teacher);
    }

    @Override
    public void withdrawStudent(Long lessonId, Long studentId) {
        Lesson lesson = lessonService.findById(lessonId);
        User student = userService.findUserById(studentId);
        lessonService.withdrawStudent(lesson, student);
    }

    @Override
    public void removeTeacher(Long lessonId, Long teacherId) {
        Lesson lesson = lessonService.findById(lessonId);
        User teacher = userService.findUserById(teacherId);
        lessonService.removeTeacher(lesson, teacher);
    }

    @Override
    public LessonDTO getLessonWithId(Long id) {
        Lesson lesson = lessonService.findById(id);
        return (lesson == null) ? null : mapper.map(lesson, LessonDTO.class);
    }

    @Override
    public List<LessonDTO> getAllLessons() {
        List<Lesson> lessons = lessonService.findAll();
        return lessons.stream()
                .map(e -> mapper.map(e, LessonDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonDTO> getLessonsByCourt(Long courtId) {
        // TODO: check whether it is correct later once the service has added listById method
        /*
        Court court = courtService.listById(courtId);
        List<Lesson> lessons = lessonService.findByCourt(court);
        return lessons.stream()
                .map(e -> mapper.map(e, LessonDTO.class))
                .collect(Collectors.toList());

         */
        return new ArrayList<>();
    }

    @Override
    public List<LessonDTO> getLessonsByStartTime(LocalDateTime startTime) {
        List<Lesson> lessons = lessonService.findByStartTime(startTime);
        return lessons.stream()
                .map(e -> mapper.map(e, LessonDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonDTO> getLessonsByEndTime(LocalDateTime endTime) {
        List<Lesson> lessons = lessonService.findByEndTime(endTime);
        return lessons.stream()
                .map(e -> mapper.map(e, LessonDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonDTO> getLessonsByLevel(Level level) {
        List<Lesson> lessons = lessonService.findByLevel(level);
        return lessons.stream()
                .map(e -> mapper.map(e, LessonDTO.class))
                .collect(Collectors.toList());
    }
}
