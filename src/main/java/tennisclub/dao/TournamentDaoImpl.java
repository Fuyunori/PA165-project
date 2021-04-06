package tennisclub.dao;

import org.springframework.stereotype.Repository;
import tennisclub.entity.Court;
import tennisclub.entity.Event;
import tennisclub.entity.Tournament;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Xuan Linh Phamová
 */
@Repository
public class TournamentDaoImpl implements TournamentDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Tournament tournament) {
        em.persist(tournament);
    }

    @Override
    public Tournament update(Tournament tournament) {
        return em.merge(tournament);
    }

    @Override
    public void remove(Tournament tournament) {
        if(!em.contains(tournament)){
            em.merge(tournament);
        }
        em.remove(tournament);
    }

    @Override
    public List<Tournament> findAll() {
        return em.createQuery("select t from Tournament t", Tournament.class)
                .getResultList();
    }

    @Override
    public Tournament findById(Long id) {
        return em.find(Tournament.class, id);
    }

    @Override
    public List<Tournament> findByCourt(Court court) {
        return em.createQuery("select t from Tournament t where t.court = :court", Tournament.class)
                .setParameter("court", court)
                .getResultList();
    }

    @Override
    public List<Tournament> findByStartTime(LocalDateTime startTime) {
        return em.createQuery("select t from Tournament t where t.startTime = :startTime", Tournament.class)
                .setParameter("startTime", startTime)
                .getResultList();
    }

    @Override
    public List<Tournament> findByEndTime(LocalDateTime endTime) {
        return em.createQuery("select t from Tournament t where t.endTime = :endTime", Tournament.class)
                .setParameter("endTime", endTime)
                .getResultList();
    }

    @Override
    public List<Tournament> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("select t from Tournament t where t.endTime > :from and t.startTime < :to", Tournament.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<Tournament> findByCapacity(Integer capacity) {
        return em.createQuery("select t from Tournament t where t.capacity = :capacity", Tournament.class)
                .setParameter("capacity", capacity)
                .getResultList();
    }

    @Override
    public List<Tournament> findByPrize(Integer prize) {
        return em.createQuery("select t from Tournament t where t.prize = :prize", Tournament.class)
                .setParameter("prize", prize)
                .getResultList();
    }
}
