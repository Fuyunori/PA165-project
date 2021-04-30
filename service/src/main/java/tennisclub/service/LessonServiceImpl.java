package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.LessonDao;
import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.enums.Level;
import tennisclub.exceptions.ServiceLayerException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LessonServiceImpl implements LessonService{
    private final LessonDao lessonDao;
    private final TimeService timeService;

    @Autowired
    public LessonServiceImpl(LessonDao lessonDao, TimeService timeService) {
        this.lessonDao = lessonDao;
        this.timeService = timeService;
    }

    @Override
    public Lesson create(Lesson lesson) {
        lessonDao.create(lesson);
        return lesson;
    }

    @Override
    public Lesson update(Lesson lesson) {
        return lessonDao.update(lesson);
    }

    @Override
    public void remove(Lesson lesson) {
        lessonDao.remove(lesson);
    }

    @Override
    public Lesson enrollStudent(Lesson lesson, User student) {
        if(lesson.getStudents().contains(student)){
            throw new ServiceLayerException("Can't enroll a student into a course in which he/she is already enrolled into!");
        }

        checkEnrollmentOpen(lesson);
        checkCapacityLimit(lesson);

        student.addLessonToAttend(lesson);
        return lessonDao.update(lesson);
    }

    @Override
    public Lesson withdrawStudent(Lesson lesson, User student) {
        if(!lesson.getStudents().contains(student)){
            throw new ServiceLayerException("Can't withdraw a student from a course in which he/she is not enrolled into!");
        }

        checkEnrollmentOpen(lesson);

        student.removeLessonToAttend(lesson);
        return lessonDao.update(lesson);
    }

    @Override
    public Lesson addTeacher(Lesson lesson, User teacher) {
        if(lesson.getTeachers().contains(teacher)){
            throw new ServiceLayerException("Can't assign a teacher to a course which he/she already teaches!");
        }

        checkEnrollmentOpen(lesson);

        teacher.addLessonToTeach(lesson);
        return lessonDao.update(lesson);
    }

    @Override
    public Lesson removeTeacher(Lesson lesson, User teacher) {
        if(!lesson.getTeachers().contains(teacher)){
            throw new ServiceLayerException("Can't remove a teacher from a course which he/she doesn't teach!");
        }

        checkEnrollmentOpen(lesson);

        teacher.removeLessonToTeach(lesson);
        return lessonDao.update(lesson);
    }

    @Override
    public Lesson replaceTeacher(Lesson lesson, User oldTeacher, User newTeacher){
        oldTeacher.removeLessonToTeach(lesson);
        newTeacher.addLessonToTeach(lesson);

        return lessonDao.update(lesson);
    }

    @Override
    public Lesson findById(Long id){
        return lessonDao.findById(id);
    }

    @Override
    public List<Lesson> findByCourt(Court court){
        return lessonDao.findByCourt(court);
    }

    @Override
    public List<Lesson> findAll(){
        return lessonDao.findAll();
    }

    @Override
    public List<Lesson> findByStartTime(LocalDateTime startTime) {
        return lessonDao.findByStartTime(startTime);
    }

    @Override
    public List<Lesson> findByEndTime(LocalDateTime endTime) {
        return lessonDao.findByEndTime(endTime);
    }

    @Override
    public List<Lesson> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return lessonDao.findByTimeInterval(from, to);
    }

    @Override
    public List<Lesson> findByCapacity(Integer capacity) {
        return lessonDao.findByCapacity(capacity);
    }

    @Override
    public List<Lesson> findByLevel(Level level) {
        return lessonDao.findByLevel(level);
    }

    private void checkEnrollmentOpen(Lesson lesson) {
        final LocalDateTime CURRENT_TIME = timeService.getCurrentDateTime();
        if (CURRENT_TIME.isAfter(lesson.getStartTime())) {
            throw new ServiceLayerException("Can't enroll/withdraw user from a lesson that doesn't allow enrollment!");
        }
    }

    private void checkCapacityLimit(Lesson lesson){
        if(lesson.getCapacity() != null) {
            final int NUMBER_OF_STUDENTS = lesson.getStudents().size();
            final int CAPACITY = lesson.getCapacity();
            if (NUMBER_OF_STUDENTS >= CAPACITY) {
                throw new ServiceLayerException("The lesson is fully occupied!");
            }
        }
    }
}
