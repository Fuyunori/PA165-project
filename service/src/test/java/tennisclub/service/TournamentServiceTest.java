package tennisclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dao.TournamentDao;
import tennisclub.entity.Court;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;
import tennisclub.entity.ranking.Ranking;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TournamentServiceTest {

    @MockBean
    private TournamentDao tournamentDao;

    @Autowired
    private TournamentService tournamentService;

    private Tournament tournament;

    @BeforeEach
    public void setup() {
        tournament = new Tournament(makeTime(5), makeTime(10), "Turnaj", 15, 10_000);
    }

    @Test
    public void createTest() {
        Tournament created = tournamentService.create(tournament);

        verify(tournamentDao).create(tournament);
        assertThat(created).isEqualTo(tournament);
    }

    @Test
    public void updateTest() {
        tournament.setCapacity(10);
        tournament.setPrize(20_000);
        tournament.setName("Somthing Else");
        when(tournamentDao.update(tournament)).thenReturn(tournament);

        Tournament updated = tournamentService.update(tournament);

        assertThat(updated).isEqualTo(tournament);
    }

    @Test
    public void removeTest() {
        tournamentService.remove(tournament);
        verify(tournamentDao).remove(tournament);
    }

    @Test
    public void findByIdTest() {
        when(tournamentDao.findById(tournament.getId())).thenReturn(tournament);

        Tournament found = tournamentService.findById(tournament.getId());

        assertThat(found).isEqualTo(tournament);
    }

    @Test
    public void findByInvalidIdTest() {
        when(tournamentDao.findById(any(Long.class))).thenReturn(tournament);

        Tournament found = tournamentService.findById(tournament.getId());

        assertThat(found).isNull();
    }

    @Test
    public void findAllTest() {
        Tournament tournament1 = new Tournament(makeTime(5), makeTime(10), "Turnaj1", 15, 10_000);
        Tournament tournament2 = new Tournament(makeTime(12), makeTime(20), "Turnaj2", 10, 5_000);
        List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);
        when(tournamentDao.findAll()).thenReturn(tournaments);

        List<Tournament> found = tournamentService.findAll();

        assertThat(found.size()).isEqualTo(2);
        assertThat(found).contains(tournament1);
        assertThat(found).contains(tournament2);
    }

    @Test
    public void findAllEmptyTest() {
        when(tournamentDao.findAll()).thenReturn(Collections.emptyList());

        List<Tournament> found = tournamentService.findAll();

        assertThat(found).isEmpty();
    }

    @Test
    public void findByCourtTest() {
        Tournament tournament1 = new Tournament(makeTime(5), makeTime(10), "Turnaj1", 15, 10_000);
        Tournament tournament2 = new Tournament(makeTime(12), makeTime(20), "Turnaj2", 10, 5_000);
        Court court = new Court();
        tournament1.setCourt(court);
        tournament2.setCourt(court);
        List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);
        when(tournamentDao.findByCourt(court)).thenReturn(tournaments);

        List<Tournament> found = tournamentService.findByCourt(court);

        assertThat(found.size()).isEqualTo(2);
        assertThat(found).contains(tournament1);
        assertThat(found).contains(tournament2);
    }

    @Test
    public void findByCourtEmptyTest() {
        Court court = new Court();
        when(tournamentDao.findByCourt(court)).thenReturn(Collections.emptyList());

        List<Tournament> found = tournamentService.findByCourt(court);

        assertThat(found).isEmpty();
    }

    @Test
    public void findByStartTimeTest() {
        when(tournamentDao.findByStartTime(tournament.getStartTime()))
                .thenReturn(Collections.singletonList(tournament));

        List<Tournament> found = tournamentService.findByStartTime(tournament.getStartTime());

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournament);
    }

    @Test
    public void findByStartTimeEmptyTest() {
        when(tournamentDao.findByStartTime(tournament.getStartTime()))
                .thenReturn(Collections.emptyList());

        List<Tournament> found = tournamentService.findByStartTime(tournament.getStartTime());

        assertThat(found).isEmpty();
    }

    @Test
    public void findByEndTimeTest() {
        when(tournamentDao.findByEndTime(tournament.getEndTime()))
                .thenReturn(Collections.singletonList(tournament));

        List<Tournament> found = tournamentService.findByEndTime(tournament.getEndTime());

        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournament);
    }

    @Test
    public void findByEndTimeEmptyTest() {
        when(tournamentDao.findByEndTime(tournament.getEndTime()))
                .thenReturn(Collections.emptyList());

        List<Tournament> found = tournamentService.findByEndTime(tournament.getEndTime());

        assertThat(found).isEmpty();
    }

    @Test
    public void findByTimeIntervalTest() {
        Tournament tournament1 = new Tournament(makeTime(5), makeTime(10), "Turnaj1", 15, 10_000);
        Tournament tournament2 = new Tournament(makeTime(12), makeTime(15), "Turnaj2", 10, 5_000);
        LocalDateTime from = makeTime(4);
        LocalDateTime to = makeTime(16);
        List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);
        when(tournamentDao.findByTimeInterval(from, to)).thenReturn(tournaments);

        List<Tournament> found = tournamentService.findByTimeInterval(from, to);

        assertThat(found.size()).isEqualTo(2);
        assertThat(found).contains(tournament1);
        assertThat(found).contains(tournament2);
    }

    @Test
    public void findByTimeIntervalEmptyTest() {
        LocalDateTime from = makeTime(4);
        LocalDateTime to = makeTime(16);
        when(tournamentDao.findByTimeInterval(from, to)).thenReturn(Collections.emptyList());

        List<Tournament> found = tournamentService.findByTimeInterval(from, to);

        assertThat(found).isEmpty();
    }

    @Test
    public void findByCapacityTest() {
        Tournament tournament1 = new Tournament(makeTime(5), makeTime(10), "Turnaj1", 15, 10_000);
        Tournament tournament2 = new Tournament(makeTime(12), makeTime(15), "Turnaj2", 15, 5_000);
        List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);
        when(tournamentDao.findByCapacity(15)).thenReturn(tournaments);

        List<Tournament> found = tournamentService.findByCapacity(15);

        assertThat(found.size()).isEqualTo(2);
        assertThat(found).contains(tournament1);
        assertThat(found).contains(tournament2);
    }

    @Test
    public void findByCapacityEmptyTest() {
        when(tournamentDao.findByCapacity(15)).thenReturn(Collections.emptyList());

        List<Tournament> found = tournamentService.findByCapacity(15);

        assertThat(found).isEmpty();
    }

    private User makeUser(String name, String userName, String email) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(userName);
        user.setName(name);
        return user;
    }

    private LocalDateTime makeTime(int hours) {
        return LocalDateTime.of(2021, 5, 3, hours, 0);
    }
}
