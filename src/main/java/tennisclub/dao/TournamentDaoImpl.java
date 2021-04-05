package tennisclub.dao;

import org.springframework.stereotype.Repository;
import tennisclub.entity.Tournament;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class TournamentDaoImpl implements TournamentDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Tournament tournament) {
        em.persist(tournament);
    }

    @Override
    public void update(Tournament tournament) {
        em.merge(tournament);
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
        return em.createQuery("select t from Tournament t where t.id = :id", Tournament.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<Tournament> findByCapacity(int capacity) {
        return em.createQuery("select t from Tournament t where t.capacity = :capacity", Tournament.class)
                .setParameter("capacity", capacity)
                .getResultList();
    }

    @Override
    public List<Tournament> findByPrize(int prize) {
        return em.createQuery("select t from Tournament t where t.prize = :prize", Tournament.class)
                .setParameter("prize", prize)
                .getResultList();
    }
}
