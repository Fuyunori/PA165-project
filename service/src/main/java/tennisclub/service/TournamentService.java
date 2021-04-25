package tennisclub.service;

import tennisclub.entity.Tournament;
import tennisclub.entity.ranking.Ranking;

import java.time.LocalDateTime;
import java.util.List;

public interface TournamentService {
    void create(Tournament tournament);
    Tournament update(Tournament tournament);
    void remove(Tournament tournament);
    List<Tournament> listByStartTime(LocalDateTime startTime);
    List<Tournament> listByEndTime(LocalDateTime endTime);
    List<Tournament> listByTimeInterval(LocalDateTime from, LocalDateTime to);
    List<Tournament> listByCapacity(Integer capacity);
    void addRanking(Tournament tournament, Ranking ranking);
}
