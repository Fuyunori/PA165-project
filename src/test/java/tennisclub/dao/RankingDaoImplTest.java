package tennisclub.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import tennisclub.entity.Court;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;
import tennisclub.entity.ranking.Ranking;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RankingDaoImplTest {
    private Tournament tournament;
    private Tournament otherTournament;
    private User user;
    private User otherUser;

    @Autowired
    RankingDao rankingDao;

    @PersistenceContext
    EntityManager manager;

    @BeforeEach
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

        otherTournament = new Tournament();
        otherTournament.setStartTime(LocalDateTime.now().plusHours(1));
        otherTournament.setEndTime(LocalDateTime.now().plusHours(2));
        otherTournament.setPrize(5000);
        otherTournament.setCourt(court);
        manager.persist(otherTournament);

        user = new User();
        user.setUsername("emil62");
        manager.persist(user);

        otherUser = new User();
        otherUser.setUsername("winston84");
        manager.persist(otherUser);
    }

    @Test
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
    void create() {
        Ranking createdRanking = new Ranking(tournament, user);
        createdRanking.setPlayerPlacement(42);
        rankingDao.create(createdRanking);

        Ranking ranking = manager.createQuery("select r from Ranking r", Ranking.class).getSingleResult();

        assertThat(ranking.getTournament()).isEqualTo(tournament);
        assertThat(ranking.getUser()).isEqualTo(user);
        assertThat(ranking.getPlayerPlacement()).isEqualTo(42);
    }

    @Test
    void find() {
        Ranking ranking1 = new Ranking(tournament, user);
        Ranking ranking2 = new Ranking(otherTournament, user);
        Ranking ranking3 = new Ranking(tournament, otherUser);
        manager.persist(ranking1);
        manager.persist(ranking2);
        manager.persist(ranking3);

        assertThat(rankingDao.find(tournament, user)).isEqualTo(ranking1);
        assertThat(rankingDao.find(otherTournament, user)).isEqualTo(ranking2);
        assertThat(rankingDao.find(tournament, otherUser)).isEqualTo(ranking3);
        assertThrows(EmptyResultDataAccessException.class, () -> rankingDao.find(otherTournament, otherUser));
    }

    @Test
    void findByTournament() {
        Ranking ranking1 = new Ranking(tournament, user);
        Ranking ranking2 = new Ranking(otherTournament, user);
        Ranking ranking3 = new Ranking(tournament, otherUser);
        manager.persist(ranking1);
        manager.persist(ranking2);
        manager.persist(ranking3);

        List<Ranking> byTournament = rankingDao.findByTournament(tournament);
        assertThat(byTournament.size()).isEqualTo(2);
        assertThat(byTournament).contains(ranking1);
        assertThat(byTournament).doesNotContain(ranking2);
        assertThat(byTournament).contains(ranking3);

        List<Ranking> byOtherTournament = rankingDao.findByTournament(otherTournament);
        assertThat(byOtherTournament.size()).isEqualTo(1);
        assertThat(byOtherTournament).doesNotContain(ranking1);
        assertThat(byOtherTournament).contains(ranking2);
        assertThat(byOtherTournament).doesNotContain(ranking3);
    }

    @Test
    void findByUser() {
        Ranking ranking1 = new Ranking(tournament, user);
        Ranking ranking2 = new Ranking(otherTournament, user);
        Ranking ranking3 = new Ranking(tournament, otherUser);
        manager.persist(ranking1);
        manager.persist(ranking2);
        manager.persist(ranking3);

        List<Ranking> byUser = rankingDao.findByUser(user);
        assertThat(byUser.size()).isEqualTo(2);
        assertThat(byUser).contains(ranking1);
        assertThat(byUser).contains(ranking2);
        assertThat(byUser).doesNotContain(ranking3);

        List<Ranking> byOtherUser = rankingDao.findByUser(otherUser);
        assertThat(byOtherUser.size()).isEqualTo(1);
        assertThat(byOtherUser).doesNotContain(ranking1);
        assertThat(byOtherUser).doesNotContain(ranking2);
        assertThat(byOtherUser).contains(ranking3);
    }

    @Test
    void update() {
        Ranking ranking = new Ranking(tournament, user);
        ranking.setPlayerPlacement(1);
        manager.persist(ranking);

        Ranking first = manager.createQuery("select r from Ranking r", Ranking.class).getSingleResult();
        assertThat(first.getPlayerPlacement()).isEqualTo(1);

        first.setPlayerPlacement(2);
        Ranking second = manager.createQuery("select r from Ranking r", Ranking.class).getSingleResult();
        assertThat(second.getPlayerPlacement()).isEqualTo(2);

        manager.detach(second);
        second.setPlayerPlacement(3);
        Ranking stillSecond = manager.createQuery("select r from Ranking r", Ranking.class).getSingleResult();
        assertThat(stillSecond.getPlayerPlacement()).isEqualTo(2);

        rankingDao.update(second);
        Ranking charlie = manager.createQuery("select r from Ranking r", Ranking.class).getSingleResult();
        assertThat(charlie.getPlayerPlacement()).isEqualTo(3);
    }
}
