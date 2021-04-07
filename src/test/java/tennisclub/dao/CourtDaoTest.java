package tennisclub.dao;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import tennisclub.entity.Court;
import tennisclub.entity.enums.CourtType;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CourtDaoTest {
	@Autowired
	private CourtDao courtDao;

	@Test
	void createFindDeleteTest() {
		Court court = new Court("Court 1");
		courtDao.create(court);

		Court foundCourt = courtDao.findById(court.getId());
		assertThat(foundCourt).isEqualTo(court);

		courtDao.delete(court);
		assertThatThrownBy(() ->
			courtDao.findById(court.getId())
		).isInstanceOf(EmptyResultDataAccessException.class);
	}

	@Test
	void createViolateConstraintTest(){
		Court court1 = new Court("Court 1");
		court1.setAddress("Torquay Squash Club, TQ2 7NS");
		courtDao.create(court1);

		Court court2 = new Court("Court 1");
		court2.setAddress("Torquay Squash Club, TQ2 7NS");
		courtDao.create(court2);

		assertThatThrownBy(() ->
			courtDao.findByAddress("TQ2 7NS")
		).isInstanceOf(DataIntegrityViolationException.class);

	}

	@Test
	void updateViolateConstraintTest(){
		Court court1 = new Court("Court 1");
		court1.setAddress("Torquay Squash Club, TQ2 7NS");
		courtDao.create(court1);

		Court court2 = new Court("Court 2");
		court2.setAddress("Torquay Squash Club, TQ2 7NS");
		courtDao.create(court2);

		courtDao.findByAddress("TQ2 7NS");

		court2.setName("Court 1");
		courtDao.update(court2);

		assertThatThrownBy(() ->
			courtDao.findByAddress("TQ2 7NS")
		).isInstanceOf(DataIntegrityViolationException.class);

	}

	@Test
	void findByAddress() {
		Court court1 = new Court("Court 1");
		court1.setAddress("Torquay Squash Club, TQ2 7NS");
		courtDao.create(court1);

		Court court2 = new Court("Court 2");
		court2.setAddress("Torquay Squash Club, TQ2 7NS");
		courtDao.create(court2);

		Court court3 = new Court("Court 1");
		court3.setAddress("Club Classic, Žabovřeská 3, 616 00 Brno");
		courtDao.create(court3);

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
		courtDao.create(court1);

		Court court2 = new Court("Court 2");
		court2.setAddress("Torquay Squash Club, TQ2 7NS");
		court2.setType(CourtType.TURF);
		courtDao.create(court2);

		Court court3 = new Court("Kurt 1");
		court3.setAddress("Club Classic, Žabovřeská 3, 616 00 Brno");
		court3.setType(CourtType.GRASS);
		courtDao.create(court3);

		List<Court> foundCourts = courtDao.findByType(CourtType.GRASS);

		assertThat(foundCourts.size()).isEqualTo(2);
		assertThat(foundCourts).contains(court1);
		assertThat(foundCourts).contains(court3);
	}

	@Test
	void updateTest() {
		Court court = new Court("Court 1");
		court.setAddress("Torquay Squash Club, TQ2 7NS");
		courtDao.create(court);

		Long originalClubId = court.getId();

		Court foundCourt = courtDao.findById(originalClubId);
		assertThat(foundCourt.getName()).isEqualTo("Court 1");
		assertThat(foundCourt.getAddress()).isEqualTo("Torquay Squash Club, TQ2 7NS");

		court.setName("Kurt 1");
		court.setAddress("Club Classic, Žabovřeská 3, 616 00 Brno");
		court = courtDao.update(court);

		foundCourt = courtDao.findById(originalClubId);
		assertThat(foundCourt.getName()).isEqualTo("Kurt 1");
		assertThat(foundCourt.getAddress()).isEqualTo("Club Classic, Žabovřeská 3, 616 00 Brno");

		assertThat(court.getName()).isEqualTo("Kurt 1");
		assertThat(court.getAddress()).isEqualTo("Club Classic, Žabovřeská 3, 616 00 Brno");
	}

}
