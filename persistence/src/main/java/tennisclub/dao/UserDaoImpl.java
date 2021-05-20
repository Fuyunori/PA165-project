package tennisclub.dao;

import org.springframework.stereotype.Repository;
import tennisclub.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


/**
 * @author Ondrej Holub
 */
@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public void create(User user) {
        em.persist(user);
    }

    @Override
    public User findById(Long id) {
        return em.createQuery("SELECT u FROM User u WHERE u.id=:id", User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<User> findByName(String name) {
        return em.createQuery("SELECT u FROM User u WHERE u.name LIKE CONCAT('%', :name, '%') ", User.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public User findByUsername(String username) {
        return em.createQuery("SELECT u FROM User u WHERE u.username=:username", User.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public List<User> findByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email=:email", User.class)
                .setParameter("email", email)
                .getResultList();
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    @Override
    public User update(User user) {
        return em.merge(user);
    }

    @Override
    public void delete(User user) {
        if (!em.contains(user)) {
            user = em.merge(user);
        }
        em.remove(user);

    }

}
