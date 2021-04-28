package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.LessonDao;
import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.entity.enums.Level;
import tennisclub.exceptions.TennisClubManagerException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
public class LessonServiceImpl implements LessonService{
    private final LessonDao lessonDao;

    @Autowired
    public LessonServiceImpl(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
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
    public void enrollStudent(Lesson lesson, User student) {
        if(lesson.getStudents().contains(student)){
            throw new TennisClubManagerException("Can't enroll a student into a course in which he/she is already enrolled into!");
        }
        student.addLessonToAttend(lesson);
    }

    @Override
    public void addTeacher(Lesson lesson, User teacher) {
        if(lesson.getTeachers().contains(teacher)){
            throw new TennisClubManagerException("Can't assign a teacher to a course which he/she already teaches!");
        }
        teacher.addLessonToTeach(lesson);
    }

    @Override
    public void withdrawStudent(Lesson lesson, User student) {
        if(!lesson.getStudents().contains(student)){
            throw new TennisClubManagerException("Can't withdraw a student from a course in which he/she is not enrolled into!");
        }
        student.removeLessonToAttend(lesson);
    }

    @Override
    public void removeTeacher(Lesson lesson, User teacher) {
        if(!lesson.getTeachers().contains(teacher)){
            throw new TennisClubManagerException("Can't remove a teacher from a course which he/she doesn't teach!");
        }
        teacher.removeLessonToTeach(lesson);
    }

    @Override
    public Lesson listById(Long id){
        return lessonDao.findById(id);
    }

    @Override
    public List<Lesson> listByCourt(Court court){
        return lessonDao.findByCourt(court);
    }

    @Override
    public List<Lesson> listAll(){
        return lessonDao.findAll();
    }

    @Override
    public List<Lesson> listByStartTime(LocalDateTime startTime) {
        return lessonDao.findByStartTime(startTime);
    }

    @Override
    public List<Lesson> listByEndTime(LocalDateTime endTime) {
        return lessonDao.findByEndTime(endTime);
    }

    @Override
    public List<Lesson> listByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return lessonDao.findByTimeInterval(from, to);
    }

    @Override
    public List<Lesson> listAllLessonsToday(){
        LocalTime startOfDay = LocalTime.of(0,0);
        LocalTime endOfDay = LocalTime.of(23,59);
        LocalDate today = LocalDate.now();

        LocalDateTime startOfToday = LocalDateTime.of(today, startOfDay);
        LocalDateTime endOfToday = LocalDateTime.of(today, endOfDay);

        return lessonDao.findByTimeInterval(startOfToday, endOfToday);
    }

    @Override
    public Set<Lesson> listByTeacher(User teacher) {
        return teacher.getLessonsToTeach();
    }

    @Override
    public Set<Lesson> listByStudent(User student) {
        return student.getLessonsToAttend();
    }

    @Override
    public List<Lesson> listByCapacity(Integer capacity) {
        return lessonDao.findByCapacity(capacity);
    }

    @Override
    public List<Lesson> listByLevel(Level level) {
        return lessonDao.findByLevel(level);
    }
}
