package tennisclub.dao;

import org.springframework.stereotype.Repository;
import tennisclub.entity.Court;
import tennisclub.entity.Lesson;
import tennisclub.enums.Level;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class LessonDaoImpl implements LessonDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Lesson lesson) {
        em.persist(lesson);
    }

    @Override
    public Lesson update(Lesson lesson) {
        return em.merge(lesson);
    }

    @Override
    public void remove(Lesson lesson) {
        if(!em.contains(lesson)){
            lesson = em.merge(lesson);
        }
        em.remove(lesson);
    }

    @Override
    public List<Lesson> findAll() {
        return em.createQuery("SELECT l FROM Lesson l", Lesson.class)
                .getResultList();
    }

    @Override
    public Lesson findById(Long id) {
        return em.find(Lesson.class, id);
    }

    @Override
    public List<Lesson> findByCourt(Court court) {
        return em.createQuery("SELECT l FROM Lesson l WHERE l.court = :court", Lesson.class)
                .setParameter("court", court)
                .getResultList();
    }

    @Override
    public List<Lesson> findByStartTime(LocalDateTime startTime) {
        return em.createQuery("SELECT l FROM Lesson l WHERE l.startTime = :startTime", Lesson.class)
                .setParameter("startTime", startTime)
                .getResultList();
    }

    @Override
    public List<Lesson> findByEndTime(LocalDateTime endTime) {
        return em.createQuery("SELECT l FROM Lesson l WHERE l.endTime = :endTime", Lesson.class)
                .setParameter("endTime", endTime)
                .getResultList();
    }

    @Override
    public List<Lesson> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("SELECT l FROM Lesson l WHERE l.endTime > :from AND l.startTime < :to", Lesson.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<Lesson> findByCapacity(Integer capacity) {
        return em.createQuery("SELECT l FROM Lesson l WHERE l.capacity = :capacity", Lesson.class)
                .setParameter("capacity", capacity)
                .getResultList();
    }

    @Override
    public List<Lesson> findByLevel(Level level) {
        return em.createQuery("SELECT l FROM Lesson l WHERE l.level = :level", Lesson.class)
                .setParameter("level", level)
                .getResultList();
    }
}
