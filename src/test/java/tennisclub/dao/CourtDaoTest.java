package tennisclub.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tennisclub.entity.Court;
import tennisclub.entity.enums.CourtType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for CourtDao
 * @author Ondřej Holub
 */
@SpringBootTest
@Transactional
class CourtDaoTest {

	@Autowired
	private CourtDao courtDao;

	@PersistenceContext
	EntityManager em;

	@Test
	void createTest() {
		Court court = new Court("Court 1");
		courtDao.create(court);
		Court foundCourt = em.find(Court.class, court.getId());
		assertThat(foundCourt).isEqualTo(court);
	}

	@Test
	void findTest() {
		Court court = new Court("Court 1");
		em.persist(court);
		Court foundCourt = courtDao.findById(court.getId());
		assertThat(foundCourt).isEqualTo(court);
	}

	@Test
	void deleteTest() {
		Court court = new Court("Court 1");
		em.persist(court);
		courtDao.delete(court);
		Court foundCourt = em.find(Court.class, court.getId());
		assertThat(foundCourt).isEqualTo(null);
	}

	@Test
	void deleteDetachedTest() {
		Court court = new Court("Court 1");
		em.persist(court);
		em.detach(court);
		courtDao.delete(court);
		Court foundCourt = em.find(Court.class, court.getId());
		assertThat(foundCourt).isEqualTo(null);
	}

	@Test
	void createViolateConstraintTest() {
		Court court1 = new Court("Court 1");
		court1.setAddress("Torquay Squash Club, TQ2 7NS");
		courtDao.create(court1);

		assertThatThrownBy(() -> {
			Court court2 = new Court("Court 1");
			court2.setAddress("Torquay Squash Club, TQ2 7NS");
			courtDao.create(court2);
			em.flush();
		}).isInstanceOf(PersistenceException.class);
	}

	@Test
	void updateViolateConstraintTest() {
		Court court1 = new Court("Court 1");
		court1.setAddress("Torquay Squash Club, TQ2 7NS");
		em.persist(court1);

		Court court2 = new Court("Court 2");
		court2.setAddress("Torquay Squash Club, TQ2 7NS");
		em.persist(court2);

		em.flush();

		assertThatThrownBy(() -> {
			court2.setName("Court 1");
			courtDao.update(court2);
			em.flush();
		}).isInstanceOf(PersistenceException.class);
	}

	@Test
	void findByAddress() {
		Court court1 = new Court("Court 1");
		court1.setAddress("Torquay Squash Club, TQ2 7NS");
		em.persist(court1);

		Court court2 = new Court("Court 2");
		court2.setAddress("Torquay Squash Club, TQ2 7NS");
		em.persist(court2);

		Court court3 = new Court("Court 1");
		court3.setAddress("Club Classic, Žabovřeská 3, 616 00 Brno");
		em.persist(court3);

		List<Court> foundCourts = courtDao.findByAddress("TQ2 7NS");

		assertThat(foundCourts.size()).isEqualTo(2);
		assertThat(foundCourts).contains(court1);
		assertThat(foundCourts).contains(court2);
	}

	@Test
	void findByType() {
		Court court1 = new Court("Court 1");
		court1.setAddress("Torquay Squash Club, TQ2 7NS");
		court1.setType(CourtType.GRASS);
		em.persist(court1);

		Court court2 = new Court("Court 2");
		court2.setAddress("Torquay Squash Club, TQ2 7NS");
		court2.setType(CourtType.TURF);
		em.persist(court2);

		Court court3 = new Court("Kurt 1");
		court3.setAddress("Club Classic, Žabovřeská 3, 616 00 Brno");
		court3.setType(CourtType.GRASS);
		em.persist(court3);

		List<Court> foundCourts = courtDao.findByType(CourtType.GRASS);

		assertThat(foundCourts.size()).isEqualTo(2);
		assertThat(foundCourts).contains(court1);
		assertThat(foundCourts).contains(court3);
	}

	@Test
	void updateTest() {
		Court court = new Court("Court 1");
		court.setAddress("Torquay Squash Club, TQ2 7NS");
		em.persist(court);

		Long originalClubId = court.getId();

		Court foundCourt = em.find(Court.class, originalClubId);
		assertThat(foundCourt.getName()).isEqualTo("Court 1");
		assertThat(foundCourt.getAddress()).isEqualTo("Torquay Squash Club, TQ2 7NS");
		assertThat(foundCourt.getType()).isEqualTo(null);

		court.setName("Kurt 1");
		court.setAddress("Club Classic, Žabovřeská 3, 616 00 Brno");
		court.setType(CourtType.CLAY);
		court = courtDao.update(court);

		foundCourt = em.find(Court.class, originalClubId);
		assertThat(foundCourt.getName()).isEqualTo("Kurt 1");
		assertThat(foundCourt.getAddress()).isEqualTo("Club Classic, Žabovřeská 3, 616 00 Brno");
		assertThat(foundCourt.getType()).isEqualTo(CourtType.CLAY);

		assertThat(court.getName()).isEqualTo("Kurt 1");
		assertThat(court.getAddress()).isEqualTo("Club Classic, Žabovřeská 3, 616 00 Brno");
		assertThat(foundCourt.getType()).isEqualTo(CourtType.CLAY);
	}

}
