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
    public Ranking find(Tournament tournament, User player) {
        return em.createQuery("SELECT r FROM Ranking r WHERE r.tournament = :t AND r.player = :p", Ranking.class)
                .setParameter("t", tournament)
                .setParameter("p", player)
                .getSingleResult();
    }

    @Override
    public List<Ranking> findByTournament(Tournament tournament) {
        return em.createQuery("SELECT r FROM Ranking r WHERE r.tournament=:tournament", Ranking.class)
                .setParameter("tournament", tournament)
                .getResultList();
    }

    @Override
    public List<Ranking> findByUser(User player) {
        return em.createQuery("SELECT r FROM Ranking r WHERE r.player=:player", Ranking.class)
                .setParameter("player", player)
                .getResultList();
    }

    @Override
    public Ranking update(Ranking ranking) {
        return em.merge(ranking);
    }

    @Override
    public void delete(Ranking ranking) {
        if (!em.contains(ranking)) {
            ranking = em.merge(ranking);
        }
        em.remove(ranking);
    }

}
