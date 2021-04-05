package tennisclub.dao;

import tennisclub.entity.Lesson;

import java.util.List;

public interface LessonDao {
    void create(Lesson lesson);
    void update(Lesson lesson);
    void remove(Lesson lesson);

    List<Lesson> findAll();
    Lesson findById(Long id);
    //List<Lesson> findByLecturerName(String lecturerName);
    List<Lesson> findByCapacity(Integer capacity);
}
