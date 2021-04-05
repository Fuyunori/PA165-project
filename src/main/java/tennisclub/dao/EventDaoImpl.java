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
@Transactional
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
        return em.createQuery("select e from Event e where e.court = :court", Event.class)
                .setParameter("court", court)
                .getResultList();
    }

    @Override
    public List<Event> findByStartTime(LocalDateTime startTime) {
        return em.createQuery("select e from Event e where e.startTime = :startTime", Event.class)
                .setParameter("startTime", startTime)
                .getResultList();
    }

    @Override
    public List<Event> findByEndTime(LocalDateTime endTime) {
        return em.createQuery("select e from Event e where e.endTime = :endTime", Event.class)
                .setParameter("endTime", endTime)
                .getResultList();
    }

    @Override
    public List<Event> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("select e from Event e where e.endTime >= :from and e.startTime <= :to", Event.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<Event> findAll() {
        return em.createQuery("select e from Event e", Event.class).getResultList();
    }

    @Override
    public void update(Event event) {
        em.merge(event);
    }

    @Override
    public void delete(Event event) {
        if (!em.contains(event)) {
            em.merge(event);
        }
        em.remove(event);
    }
}
