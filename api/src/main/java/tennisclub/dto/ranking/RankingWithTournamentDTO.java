package tennisclub.dto.ranking;


import tennisclub.dto.tournament.TournamentDTO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Ranking DTO describing the ranking from the perspective of a player
 * @author Ondrej Holub
 */
public class RankingWithTournamentDTO {

    @NotNull(message = "{ranking.tournament.notNull}")
    private TournamentDTO tournament;

    @Min(value = 1, message = "{ranking.player.placement.min}")
    private Integer playerPlacement;

    public TournamentDTO getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDTO tournament) {
        this.tournament = tournament;
    }

    public Integer getPlayerPlacement() {
        return playerPlacement;
    }

    public void setPlayerPlacement(Integer playerPlacement) {
        this.playerPlacement = playerPlacement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RankingWithTournamentDTO)) return false;

        RankingWithTournamentDTO that = (RankingWithTournamentDTO) o;

        return Objects.equals(getPlayerPlacement(), that.getPlayerPlacement()) &&
                Objects.equals(getTournament(), that.getTournament());
    }

    @Override
    public int hashCode() {
        int result = getTournament() != null ? getTournament().hashCode() : 0;
        result = 31 * result + (getPlayerPlacement() != null ? getPlayerPlacement() : 0);
        return result;
    }
}
