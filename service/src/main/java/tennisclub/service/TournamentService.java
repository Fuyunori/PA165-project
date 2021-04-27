package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Tournament;
import tennisclub.entity.ranking.Ranking;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface TournamentService {
    Tournament create(Tournament tournament);
    Tournament update(Tournament tournament);
    void remove(Tournament tournament);

    void addRanking(Tournament tournament, Ranking ranking);

    Tournament listById(Long id);
    List<Tournament> listAll();
    List<Tournament> listByStartTime(LocalDateTime startTime);
    List<Tournament> listByEndTime(LocalDateTime endTime);
    List<Tournament> listByTimeInterval(LocalDateTime from, LocalDateTime to);
    List<Tournament> listByCapacity(Integer capacity);
}
