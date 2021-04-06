package tennisclub.dao;

import org.springframework.stereotype.Repository;
import tennisclub.entity.ranking.Ranking;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Ondrej Holub
 */
@Repository
public class RankingDaoImpl implements RankingDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Ranking ranking) {
        em.persist(ranking);
    }

    @Override
    public Ranking find(Tournament tournament, User user) {
        return em.createQuery("select r from Ranking r", Ranking.class).getSingleResult();
    }

    @Override
    public List<Ranking> findByTournament(Tournament tournament) {
        return em.createQuery("select r from Ranking r where r.tournament=:tournament", Ranking.class)
                .setParameter("tournament", tournament)
                .getResultList();
    }

    @Override
    public List<Ranking> findByUser(User user) {
        return em.createQuery("select r from Ranking r where r.user=:user", Ranking.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public void update(Ranking ranking) {
        em.merge(ranking);
    }

    @Override
    public void delete(Ranking ranking) {
        em.remove(ranking);
    }

}
