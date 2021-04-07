package tennisclub.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import tennisclub.entity.Court;
import tennisclub.entity.Tournament;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Miroslav Demek
 */
@SpringBootTest
public class TournamentDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TournamentDao tournamentDao;

    private Court court;
    private Tournament tournament;

    @BeforeEach
    @Transactional
    public void before() {
        court = new Court();
        court.setName("Pretty nice court");
        court.setAddress("Brno");
        em.persist(court);

        tournament = new Tournament(LocalDateTime.of(2021, 3, 2, 13, 0 ),
                LocalDateTime.of(2021, 3, 2, 14, 0 ), 15, 5_000);
        tournament.setCourt(court);
        em.persist(tournament);
    }


    @Test
    @Transactional
    public void testCreate() {
        Tournament created = new Tournament(LocalDateTime.of(2021, 4, 6, 13, 0 ),
                LocalDateTime.of(2021, 4, 6, 14, 0 ), 10, 20_000);
        created.setCourt(court);

        tournamentDao.create(created);

        Tournament found = em.find(Tournament.class, created.getId());
        assertThat(found).usingRecursiveComparison().isEqualTo(created);
    }

    @Test
    @Transactional
    public void testCreateWithNullCourt() {
        Tournament created = new Tournament(LocalDateTime.of(2021, 4, 6, 13, 0 ),
                LocalDateTime.of(2021, 4, 6, 14, 0 ), 10, 20_000);
        created.setCourt(null);

        assertThatThrownBy(() -> tournamentDao.create(created))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Transactional
    public void testCreateWithNullStartTime() {
        Tournament created = new Tournament(null,
                LocalDateTime.of(2021, 4, 6, 14, 0 ), 10, 20_000);
        created.setCourt(court);

        assertThatThrownBy(() -> tournamentDao.create(created))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Transactional
    public void testCreateWithNullEndTime() {
        Tournament created = new Tournament(LocalDateTime.of(2021, 4, 6, 13, 0 ),
                null, 10, 20_000);
        created.setCourt(court);

        assertThatThrownBy(() -> tournamentDao.create(created))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Transactional
    public void testCreateWithStartTimeAfterEndTime() {
        Tournament created = new Tournament(LocalDateTime.of(2021, 4, 7, 15, 0 ),
                LocalDateTime.of(2021, 4, 6, 14, 0 ), 10, 20_000);
        created.setCourt(court);

        assertThatThrownBy(() -> tournamentDao.create(created))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Transactional
    public void testUpdate() {
        Court newCourt = new Court();
        newCourt.setAddress("Praha");
        newCourt.setName("Not so nice court");
        em.persist(newCourt);

        tournament.setCourt(newCourt);
        tournament.setStartTime(LocalDateTime.of(2021, 4, 6, 12, 0 ));
        tournament.setStartTime(LocalDateTime.of(2021, 4, 6, 13, 0 ));
        tournament.setCapacity(60);
        tournament.setPrize(100);

        tournament = tournamentDao.update(tournament);

        Tournament found = em.find(Tournament.class, tournament.getId());
        assertThat(found).usingRecursiveComparison().isEqualTo(tournament);
    }

    @Test
    @Transactional
    public void testRemove() {
        tournamentDao.remove(tournament);
        List<Tournament> found = em.createQuery("select t from Tournament t", Tournament.class).getResultList();
        assertThat(tournament).isNotIn(found);
    }

    @Test
    @Transactional
    public void testFindAll() {
        Tournament anotherTournament = new Tournament(LocalDateTime.of(2021, 5, 9, 5, 30 ),
                LocalDateTime.of(2021, 5, 9, 6, 0 ), 32, 2_000);
        anotherTournament.setCourt(court);
        em.persist(anotherTournament);

        List<Tournament> found = tournamentDao.findAll();

        assertThat(found.size()).isEqualTo(2);
        assertThat(found).contains(tournament);
        assertThat(found).contains(anotherTournament);
    }

    @Test
    @Transactional
    public void testFindById() {
        Tournament found = tournamentDao.findById(tournament.getId());
        assertThat(found).usingRecursiveComparison().isEqualTo(tournament);
    }

    @Test
    @Transactional
    public void testFindByCourt() {
        List<Tournament> found = tournamentDao.findByCourt(tournament.getCourt());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournament);
    }

    @Test
    @Transactional
    public void testFindByStartTime() {
        List<Tournament> found = tournamentDao.findByStartTime(tournament.getStartTime());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournament);
    }

    @Test
    @Transactional
    public void testFindByEndTime() {
        List<Tournament> found = tournamentDao.findByEndTime(tournament.getEndTime());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournament);
    }

    @Test
    @Transactional
    public void testFindByCapacity() {
        List<Tournament> found = tournamentDao.findByCapacity(tournament.getCapacity());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournament);
    }

    @Test
    @Transactional
    public void testFindByPrize() {
        List<Tournament> found = tournamentDao.findByPrize(tournament.getPrize());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournament);
    }

    @Test
    @Transactional
    public void testFindByIntervalContainedInTournament() {
        Tournament expected = createTournamentFromTime(
                LocalDateTime.of(2022, 10, 5, 12, 30),
                LocalDateTime.of(2022, 10, 5, 14, 30)
        );
        em.persist(expected);

        List<Tournament> found = tournamentDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 13, 0),
                LocalDateTime.of(2022, 10, 5, 14, 0)
        );

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(expected);
    }

    @Test
    @Transactional
    public void testFindTournamentContainedInInterval() {
        Tournament expected = createTournamentFromTime(
                LocalDateTime.of(2022, 10, 5, 13, 0),
                LocalDateTime.of(2022, 10, 5, 14, 0)
        );
        em.persist(expected);

        List<Tournament> found = tournamentDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 12, 0),
                LocalDateTime.of(2022, 10, 5, 15, 0)
        );

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(expected);
    }

    @Test
    @Transactional
    public void testFindTournamentIntersectingIntervalFromLeft() {
        Tournament expected = createTournamentFromTime(
                LocalDateTime.of(2022, 10, 5, 13, 0),
                LocalDateTime.of(2022, 10, 5, 14, 30)
        );
        em.persist(expected);

        List<Tournament> found = tournamentDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 14, 0),
                LocalDateTime.of(2022, 10, 5, 15, 0)
        );

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(expected);
    }

    @Test
    @Transactional
    public void testFindTournamentIntersectingIntervalFromRight() {
        Tournament expected = createTournamentFromTime(
                LocalDateTime.of(2022, 10, 5, 14, 30),
                LocalDateTime.of(2022, 10, 5, 16, 0)
        );
        em.persist(expected);

        List<Tournament> found = tournamentDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 14, 0),
                LocalDateTime.of(2022, 10, 5, 15, 0)
        );

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(expected);
    }

    @Test
    @Transactional
    public void testFindByIntervalExclusivity() {
        Tournament expected = createTournamentFromTime(
                LocalDateTime.of(2022, 10, 5, 14, 0),
                LocalDateTime.of(2022, 10, 5, 16, 0)
        );
        em.persist(expected);

        List<Tournament> foundLeft = tournamentDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 13, 0),
                LocalDateTime.of(2022, 10, 5, 14, 0)
        );

        List<Tournament> foundRight = tournamentDao.findByTimeInterval(
                LocalDateTime.of(2022, 10, 5, 16, 0),
                LocalDateTime.of(2022, 10, 5, 17, 0)
        );

        assertThat(foundLeft).isEmpty();
        assertThat(foundRight).isEmpty();
    }


    private Tournament createTournamentFromTime(LocalDateTime start, LocalDateTime end) {
        Tournament t = new Tournament(start, end, 10, 10_000);
        t.setCourt(court);
        return t;
    }

}
