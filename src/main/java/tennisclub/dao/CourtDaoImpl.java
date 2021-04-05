package tennisclub.dao;

import org.springframework.stereotype.Repository;
import tennisclub.entity.Court;
import tennisclub.entity.enums.CourtType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class CourtDaoImpl implements CourtDao {
    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public void create(Court court) {
        manager.persist(court);
    }

    @Override
    @Transactional
    public void delete(Court court) {
        manager.remove(court);
    }

    @Override
    public Court findById(Long id) {
        return manager
                .createQuery("select c from Court c where c.id = :id", Court.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<Court> findByAddress(String address) {
        return manager
                .createQuery("select c from Court c where c.address like concat('%', :a, '%')", Court.class)
                .setParameter("a", address)
                .getResultList();
    }

    @Override
    public List<Court> findByType(CourtType type) {
        return manager
                .createQuery("select c from Court c where c.type = :t", Court.class)
                .setParameter("t", type)
                .getResultList();
    }
}
