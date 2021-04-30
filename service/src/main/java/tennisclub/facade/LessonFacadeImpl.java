package tennisclub.facade;

import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisclub.dto.lesson.LessonCreateDTO;
import tennisclub.dto.lesson.LessonFullDTO;
import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.enums.Level;
import tennisclub.exceptions.FacadeLayerException;
import tennisclub.service.CourtService;
import tennisclub.service.LessonService;
import tennisclub.service.UserService;

import java.time.LocalDateTime;
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

        if (!courtService.isFree(lesson.getCourt(), lesson.getStartTime(), lesson.getEndTime())) {
            throw new FacadeLayerException("Can't make a lesson. Court is not free at this time.");
        }

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
    public LessonFullDTO getLessonWithId(Long id) {
        Lesson lesson = lessonService.findById(id);
        return (lesson == null) ? null : mapper.map(lesson, LessonFullDTO.class);
    }

    @Override
    public List<LessonFullDTO> getAllLessons() {
        List<Lesson> lessons = lessonService.findAll();
        return lessons.stream()
                .map(e -> mapper.map(e, LessonFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonFullDTO> getLessonsByCourt(Long courtId) {
        Court court = courtService.getById(courtId);
        List<Lesson> lessons = lessonService.findByCourt(court);
        return lessons.stream()
                .map(e -> mapper.map(e, LessonFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonFullDTO> getLessonsByStartTime(LocalDateTime startTime) {
        List<Lesson> lessons = lessonService.findByStartTime(startTime);
        return lessons.stream()
                .map(e -> mapper.map(e, LessonFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonFullDTO> getLessonsByEndTime(LocalDateTime endTime) {
        List<Lesson> lessons = lessonService.findByEndTime(endTime);
        return lessons.stream()
                .map(e -> mapper.map(e, LessonFullDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonFullDTO> getLessonsByLevel(Level level) {
        List<Lesson> lessons = lessonService.findByLevel(level);
        return lessons.stream()
                .map(e -> mapper.map(e, LessonFullDTO.class))
                .collect(Collectors.toList());
    }
}
