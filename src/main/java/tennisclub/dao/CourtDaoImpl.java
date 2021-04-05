package tennisclub.dao;

import org.springframework.stereotype.Repository;
import tennisclub.entity.Court;

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
    public List<Court> findByAddress(String location) {
        return manager
                .createQuery("select c from Court c where c.address like :l", Court.class)
                .setParameter("l", location)
                .getResultList();
    }
}
