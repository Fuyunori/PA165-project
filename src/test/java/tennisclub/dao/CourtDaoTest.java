package tennisclub.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tennisclub.entity.Court;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CourtDaoTest {
	@Autowired
	private CourtDao courtDao;

	@Test
	void testTests() {
		Court court = new Court();
		court.setAddress("Hell");
		courtDao.create(court);

		List<Court> courts = courtDao.findByLocation("Hell");
		assertThat(courts.size()).isEqualTo(1);
		assertThat(courts.get(0).getAddress()).isEqualTo("Hell");
	}
}
