package tennisclub.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tennisclub.entity.Court;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CourtDaoTest {
	@Autowired
	private CourtDao courtDao;

	@Test
	@Transactional
	void testTests() {
		Court court = new Court();
		court.setAddress("Hell");
		courtDao.create(court);

		List<Court> courts = courtDao.findByAddress("Hell");
		assertThat(courts.size()).isEqualTo(1);
		assertThat(courts.get(0).getAddress()).isEqualTo("Hell");
	}
}
