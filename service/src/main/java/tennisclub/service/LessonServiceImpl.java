package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.LessonDao;
import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.enums.Level;
import tennisclub.exceptions.TennisClubManagerException;

import java.time.LocalDateTime;
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
    public Lesson enrollStudent(Lesson lesson, User student) {
        if(lesson.getStudents().contains(student)){
            throw new TennisClubManagerException("Can't enroll a student into a course in which he/she is already enrolled into!");
        }

        final LocalDateTime CURRENT_TIME = LocalDateTime.now();
        if(CURRENT_TIME.isAfter(lesson.getEndTime())){
            throw new TennisClubManagerException("Can't enroll a student into a course which has already ended!");
        }

        student.addLessonToAttend(lesson);
        return lessonDao.update(lesson);
    }

    @Override
    public Lesson addTeacher(Lesson lesson, User teacher) {
        if(lesson.getTeachers().contains(teacher)){
            throw new TennisClubManagerException("Can't assign a teacher to a course which he/she already teaches!");
        }

        final LocalDateTime CURRENT_TIME = LocalDateTime.now();
        if(CURRENT_TIME.isAfter(lesson.getEndTime())){
            throw new TennisClubManagerException("Can't enroll a student into a course which has already ended!");
        }

        teacher.addLessonToTeach(lesson);
        return lessonDao.update(lesson);
    }

    @Override
    public Lesson withdrawStudent(Lesson lesson, User student) {
        if(!lesson.getStudents().contains(student)){
            throw new TennisClubManagerException("Can't withdraw a student from a course in which he/she is not enrolled into!");
        }

        final LocalDateTime CURRENT_TIME = LocalDateTime.now();
        if(CURRENT_TIME.isAfter(lesson.getEndTime())){
            throw new TennisClubManagerException("Can't enroll a student into a course which has already ended!");
        }

        student.removeLessonToAttend(lesson);
        return lessonDao.update(lesson);
    }

    @Override
    public Lesson removeTeacher(Lesson lesson, User teacher) {
        if(!lesson.getTeachers().contains(teacher)){
            throw new TennisClubManagerException("Can't remove a teacher from a course which he/she doesn't teach!");
        }

        final LocalDateTime CURRENT_TIME = LocalDateTime.now();
        if(CURRENT_TIME.isAfter(lesson.getEndTime())){
            throw new TennisClubManagerException("Can't enroll a student into a course which has already ended!");
        }

        teacher.removeLessonToTeach(lesson);
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
}
