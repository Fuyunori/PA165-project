package tennisclub.dao;

import org.springframework.stereotype.Repository;
import tennisclub.entity.Court;
import tennisclub.enums.CourtType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Pavel Tobias
 */
@Repository
public class CourtDaoImpl implements CourtDao {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public void create(Court court) {
        manager.persist(court);
    }

    @Override
    public void delete(Court court) {
        if (!manager.contains(court)){
            court = manager.merge(court);
        }
        manager.remove(court);
    }

    @Override
    public List<Court> findAll() {
        return manager
                .createQuery("SELECT c FROM Court c", Court.class)
                .getResultList();
    }

    @Override
    public Court findById(Long id) {
        return manager
                .createQuery("SELECT c FROM Court c WHERE c.id = :id", Court.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<Court> findByAddress(String addressSubstr) {
        return manager
                .createQuery("SELECT c FROM Court c WHERE c.address LIKE CONCAT('%', :a, '%') ", Court.class)
                .setParameter("a", addressSubstr)
                .getResultList();
    }

    @Override
    public List<Court> findByType(CourtType type) {
        return manager
                .createQuery("SELECT c FROM Court c WHERE c.type = :t", Court.class)
                .setParameter("t", type)
                .getResultList();
    }

    @Override
    public Court update(Court court) {
        return manager.merge(court);
    }
}
