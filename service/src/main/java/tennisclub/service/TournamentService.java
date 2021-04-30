package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Court;
import tennisclub.entity.Tournament;
import tennisclub.entity.User;
import tennisclub.entity.ranking.Ranking;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface TournamentService {
    Tournament create(Tournament tournament);
    Tournament update(Tournament tournament);
    void remove(Tournament tournament);

    Tournament findById(Long id);
    List<Tournament> findAll();
    List<Tournament> findByCourt(Court court);
    List<Tournament> findByStartTime(LocalDateTime startTime);
    List<Tournament> findByEndTime(LocalDateTime endTime);
    List<Tournament> findByTimeInterval(LocalDateTime from, LocalDateTime to);
    List<Tournament> findByCapacity(Integer capacity);

    List<Ranking> findRanking(Tournament tournament, User player);
    List<Ranking> findRankingByTournament(Tournament tournament);
    List<Ranking> findRankingByPlayer(User player);
    void enrollPlayer(Tournament tournament, User player);
    Ranking rankPlayer(Ranking ranking, Integer newPlacement);
}
