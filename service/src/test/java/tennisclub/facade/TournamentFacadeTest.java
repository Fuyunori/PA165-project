package tennisclub.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import tennisclub.dto.court.CourtDto;
import tennisclub.dto.ranking.RankingWithPlayerDTO;
import tennisclub.dto.tournament.TournamentCreateDTO;
import tennisclub.dto.tournament.TournamentFullDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.entity.Court;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;
import tennisclub.entity.ranking.Ranking;
import tennisclub.exceptions.FacadeLayerException;
import tennisclub.service.CourtService;
import tennisclub.service.TournamentService;
import tennisclub.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.iterator;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Miroslav Demek
 */
@SpringBootTest
public class TournamentFacadeTest {

    @MockBean
    private TournamentService tournamentService;
    @MockBean
    private UserService userService;
    @MockBean
    private CourtService courtService;

    @Autowired
    private TournamentFacade tournamentFacade;

    private Tournament tournament;
    private TournamentFullDTO tournamentDTO;
    private Court court;
    private CourtDto courtDto;
    private User user;
    private UserDTO userDTO;
    private Ranking ranking;
    private RankingWithPlayerDTO rankingDTO;

    @BeforeEach
    public void setup() {
        court = new Court();
        court.setName("Epic court");
        court.setAddress("Some address");
        court.setId(55L);

        tournament = new Tournament(makeTime(10), makeTime(16), "Epic tournament", 15, 10_000);
        tournament.setId(7L);
        tournament.setCourt(court);

        user = makeUser("Nekdo", "Nekdovic", "nekdomir@nekde.com");
        userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());

        courtDto = new CourtDto();
        courtDto.setName(court.getName());
        courtDto.setAddress(court.getAddress());
        courtDto.setId(court.getId());

        tournamentDTO = new TournamentFullDTO();
        tournamentDTO.setCapacity(tournament.getCapacity());
        tournamentDTO.setPrize(tournament.getPrize());
        tournamentDTO.setName(tournament.getName());
        tournamentDTO.setStartTime(tournament.getStartTime());
        tournamentDTO.setEndTime(tournament.getEndTime());
        tournamentDTO.setCourt(courtDto);
        tournament.setId(tournament.getId());

        ranking = new Ranking(tournament, user);
        rankingDTO = new RankingWithPlayerDTO();
        rankingDTO.setPlayer(userDTO);
    }

    @Test
    public void createTournamentTest() {
        TournamentCreateDTO createDTO = new TournamentCreateDTO();
        createDTO.setCapacity(tournament.getCapacity());
        createDTO.setPrize(tournament.getPrize());
        createDTO.setName(tournament.getName());
        createDTO.setStartTime(tournament.getStartTime());
        createDTO.setEndTime(tournament.getEndTime());
        createDTO.setCourt(courtDto);

        when(courtService.isFree(court, tournament.getStartTime(), tournament.getEndTime())).thenReturn(true);
        when(tournamentService.create(tournament)).thenReturn(tournament);

        Long id = tournamentFacade.createTournament(createDTO);

        verify(courtService).isFree(court, tournament.getStartTime(), tournament.getEndTime());
        verify(tournamentService).create(tournament);
        assertThat(id).isEqualTo(tournament.getId());
    }

    @Test
    public void createTournamentCourtNotFreeTest() {
        TournamentCreateDTO createDTO = new TournamentCreateDTO();
        createDTO.setCapacity(tournament.getCapacity());
        createDTO.setPrize(tournament.getPrize());
        createDTO.setName(tournament.getName());
        createDTO.setStartTime(tournament.getStartTime());
        createDTO.setEndTime(tournament.getEndTime());
        createDTO.setCourt(courtDto);

        when(courtService.isFree(court, tournament.getStartTime(), tournament.getEndTime())).thenReturn(false);
        when(tournamentService.create(tournament)).thenReturn(tournament);

        assertThatThrownBy(() -> tournamentFacade.createTournament(createDTO))
                .isInstanceOf(FacadeLayerException.class);
    }

    @Test
    public void deleteTournamentTest() {
        when(tournamentService.findById(tournament.getId())).thenReturn(tournament);

        tournamentFacade.deleteTournament(tournament.getId());

        verify(tournamentService).findById(tournament.getId());
        verify(tournamentService).remove(tournament);
    }

    @Test
    public void enrollPlayerTest() {
        when(tournamentService.findById(tournament.getId())).thenReturn(tournament);
        when(userService.findUserById(user.getId())).thenReturn(user);

        tournamentFacade.enrollPlayer(tournament.getId(), user.getId());

        verify(tournamentService).findById(tournament.getId());
        verify(userService).findUserById(user.getId());
        verify(tournamentService).enrollPlayer(tournament, user);
    }

    @Test
    public void withdrawPlayerTest() {
        when(tournamentService.findById(tournament.getId())).thenReturn(tournament);
        when(userService.findUserById(user.getId())).thenReturn(user);

        tournamentFacade.withdrawPlayer(tournament.getId(), user.getId());

        verify(tournamentService).findById(tournament.getId());
        verify(userService).findUserById(user.getId());
        verify(tournamentService).withdrawPlayer(tournament, user);
    }

    @Test
    public void rankPlayerTest() {
        when(tournamentService.findById(tournament.getId())).thenReturn(tournament);
        when(userService.findUserById(user.getId())).thenReturn(user);

        tournamentFacade.rankPlayer(tournament.getId(), user.getId(), 2);

        verify(tournamentService).findById(tournament.getId());
        verify(userService).findUserById(user.getId());
        verify(tournamentService).rankPlayer(tournament, user, 2);
    }

    @Test
    public void getRankingsOfTournamentTest() {
        when(tournamentService.findById(tournament.getId())).thenReturn(tournament);

        List<RankingWithPlayerDTO> found = tournamentFacade.getRankingsOfTournament(tournament.getId());

        verify(tournamentService).findById(tournament.getId());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(rankingDTO);
    }

    @Test
    public void getTournamentWithIdTest() {
        when(tournamentService.findById(tournament.getId())).thenReturn(tournament);

        TournamentFullDTO found = tournamentFacade.getTournamentWithId(tournament.getId());

        verify(tournamentService).findById(tournament.getId());
        assertThat(found).isEqualTo(tournamentDTO);
    }

    @Test
    public void getAllTournamentsTest() {
        when(tournamentService.findAll()).thenReturn(Collections.singletonList(tournament));

        List<TournamentFullDTO> found = tournamentFacade.getAllTournaments();

        verify(tournamentService).findAll();
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournamentDTO);
    }

    @Test
    public void getTournamentsByStartTimeTest() {
        when(tournamentService.findByStartTime(tournament.getStartTime()))
                .thenReturn(Collections.singletonList(tournament));

        List<TournamentFullDTO> found = tournamentFacade.getTournamentsByStartTime(tournament.getStartTime());

        verify(tournamentService).findByStartTime(tournament.getStartTime());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournamentDTO);
    }

    @Test
    public void getTournamentsByEndTimeTest() {
        when(tournamentService.findByEndTime(tournament.getEndTime()))
                .thenReturn(Collections.singletonList(tournament));

        List<TournamentFullDTO> found = tournamentFacade.getTournamentsByEndTime(tournament.getEndTime());

        verify(tournamentService).findByEndTime(tournament.getEndTime());
        assertThat(found.size()).isEqualTo(1);
        assertThat(found).contains(tournamentDTO);
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
