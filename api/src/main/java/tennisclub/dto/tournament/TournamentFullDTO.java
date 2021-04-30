package tennisclub.dto.tournament;

import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.dto.ranking.RankingWithPlayerDTO;

import java.util.HashSet;
import java.util.Set;

public class TournamentFullDTO extends EventWithCourtDTO {
    private String name;
    private Integer capacity;
    private Integer prize;

    private Set<RankingWithPlayerDTO> playerPlacements = new HashSet<>();

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

    public Set<RankingWithPlayerDTO> getPlayerPlacements() { return playerPlacements; }

    public void setPlayerPlacements(Set<RankingWithPlayerDTO> playerPlacements) { this.playerPlacements = playerPlacements; }
}
