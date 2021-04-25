package tennisclub.service;

import tennisclub.entity.Tournament;

import java.time.LocalDateTime;
import java.util.List;

public interface TournamentService {
    void create(Tournament tournament);
    Tournament update(Tournament tournament);
    void remove(Tournament tournament);
    List<Tournament> listByStartTime(LocalDateTime startTime);
    List<Tournament> listByEndTime(LocalDateTime endTime);
}
