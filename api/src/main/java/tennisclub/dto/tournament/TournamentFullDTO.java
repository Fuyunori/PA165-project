package tennisclub.dto.tournament;

import tennisclub.dto.event.EventDTO;
import tennisclub.dto.ranking.TournamentRankingDTO;

import java.util.HashSet;
import java.util.Set;

public class TournamentFullDTO extends EventDTO {
    private String name;
    private Integer capacity;
    private Integer prize;

    private Set<TournamentRankingDTO> playerPlacements = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getPrize() {
        return prize;
    }

    public void setPrize(Integer prize) {
        this.prize = prize;
    }

    public Set<TournamentRankingDTO> getPlayerPlacements() { return playerPlacements; }

    public void setPlayerPlacements(Set<TournamentRankingDTO> playerPlacements) { this.playerPlacements = playerPlacements; }
}
