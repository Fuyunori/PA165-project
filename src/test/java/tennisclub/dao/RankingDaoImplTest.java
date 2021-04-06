package tennisclub.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tennisclub.entity.Court;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;
import tennisclub.entity.ranking.Ranking;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RankingDaoImplTest {
    private Tournament tournament;
    private User user;

    @Autowired
    RankingDao rankingDao;

    @PersistenceContext
    EntityManager manager;

    @BeforeEach()
    void initRankingDeps() {
        Court court = new Court();
        court.setName("My Court");
        manager.persist(court);

        tournament = new Tournament();
        tournament.setStartTime(LocalDateTime.now());
        tournament.setEndTime(LocalDateTime.now().plusHours(1));
        tournament.setPrize(10000);
        tournament.setCourt(court);
        manager.persist(tournament);

        user = new User();
        user.setUsername("emil62");
        manager.persist(user);
    }

    @Test
    @Transactional
    void nullTournament() {
        assertThrows(
                ConstraintViolationException.class,
                () -> {
                    rankingDao.create(new Ranking(null, user));
                    manager.flush();
                }
        );
    }

    @Test
    @Transactional
    void nullUser() {
        assertThrows(
                ConstraintViolationException.class,
                () -> {
                    rankingDao.create(new Ranking(tournament, null));
                    manager.flush();
                }
        );
    }

    @Test
    @Transactional
    void create() {
        Ranking createdRanking = new Ranking(tournament, user);
        createdRanking.setPlayerPlacement(42);
        rankingDao.create(createdRanking);

        Ranking ranking = manager.createQuery("select r from Ranking r", Ranking.class).getSingleResult();

        assertThat(ranking.getTournament()).isEqualTo(tournament);
        assertThat(ranking.getUser()).isEqualTo(user);
        assertThat(ranking.getPlayerPlacement()).isEqualTo(42);
    }
}