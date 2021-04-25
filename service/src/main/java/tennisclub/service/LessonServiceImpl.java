package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.LessonDao;
import tennisclub.entity.Lesson;
import tennisclub.entity.User;
import tennisclub.entity.enums.Level;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LessonServiceImpl implements LessonService{
    private final LessonDao lessonDao;

    @Autowired
    public LessonServiceImpl(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }

    @Override
    public void create(Lesson lesson) {
        lessonDao.create(lesson);
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
    public List<Lesson> findByCapacity(Integer capacity) {
        return lessonDao.findByCapacity(capacity);
    }

    @Override
    public List<Lesson> findByLevel(Level level) {
        return lessonDao.findByLevel(level);
    }

    @Override
    public void enrollStudent(Lesson lesson, User student) {
        // TODO: check whether the student isn't already enrolled
        lesson.addStudent(student);
    }

    @Override
    public void addTeacher(Lesson lesson, User teacher) {
        lesson.addTeacher(teacher);
    }
}
