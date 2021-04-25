package tennisclub.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tennisclub.entity.Court;
import tennisclub.entity.Event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miroslav Demek
 */
@Repository
public class EventDaoImpl implements EventDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Event event) {
        em.persist(event);
    }

    @Override
    public Event findById(Long id) {
        return em.find(Event.class, id);
    }

    @Override
    public List<Event> findByCourt(Court court) {
        return em.createQuery("SELECT e FROM Event e WHERE e.court = :court", Event.class)
                .setParameter("court", court)
                .getResultList();
    }

    @Override
    public List<Event> findByStartTime(LocalDateTime startTime) {
        return em.createQuery("SELECT e FROM Event e WHERE e.startTime = :startTime", Event.class)
                .setParameter("startTime", startTime)
                .getResultList();
    }

    @Override
    public List<Event> findByEndTime(LocalDateTime endTime) {
        return em.createQuery("SELECT e FROM Event e WHERE e.endTime = :endTime", Event.class)
                .setParameter("endTime", endTime)
                .getResultList();
    }

    @Override
    public List<Event> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("SELECT e FROM Event e WHERE e.endTime > :from AND e.startTime < :to", Event.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<Event> findAll() {
        return em.createQuery("SELECT e FROM Event e", Event.class).getResultList();
    }

    @Override
    public Event update(Event event) {
        return em.merge(event);
    }

    @Override
    public void delete(Event event) {
        if (!em.contains(event)) {
            event = em.merge(event);
        }
        em.remove(event);
    }
}
