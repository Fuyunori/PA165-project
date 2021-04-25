package tennisclub.dao;

import org.springframework.stereotype.Repository;
import tennisclub.entity.Booking;
import tennisclub.entity.Court;
import tennisclub.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Miroslav Demek
 */
@Repository
public class BookingDaoImpl implements BookingDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Booking booking) {
        em.persist(booking);
    }

    @Override
    public Booking findById(Long id) {
        return em.find(Booking.class, id);
    }

    @Override
    public List<Booking> findByCourt(Court court) {
        return em.createQuery("SELECT b FROM Booking b WHERE b.court = :court", Booking.class)
                .setParameter("court", court)
                .getResultList();
    }

    @Override
    public List<Booking> findByStartTime(LocalDateTime startTime) {
        return em.createQuery("SELECT b FROM Booking b WHERE b.startTime = :startTime", Booking.class)
                .setParameter("startTime", startTime)
                .getResultList();
    }

    @Override
    public List<Booking> findByEndTime(LocalDateTime endTime) {
        return em.createQuery("SELECT b FROM Booking b WHERE b.endTime = :endTime", Booking.class)
                .setParameter("endTime", endTime)
                .getResultList();
    }

    @Override
    public List<Booking> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("SELECT b FROM Booking b WHERE b.endTime > :from AND b.startTime < :to", Booking.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<Booking> findByUser(User user) {
        return em.createQuery("SELECT b FROM Booking b JOIN b.users u ON u = :user", Booking.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<Booking> findAll() {
        return em.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
    }

    @Override
    public Booking update(Booking booking) {
        return em.merge(booking);
    }

    @Override
    public void delete(Booking booking) {
        if (!em.contains(booking)) {
            booking = em.merge(booking);
        }
        em.remove(booking);
    }
}
