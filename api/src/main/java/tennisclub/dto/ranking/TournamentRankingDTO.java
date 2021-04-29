package tennisclub.dto.ranking;

import tennisclub.dto.UserDTO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TournamentRankingDTO {

    @NotNull
    private UserDTO player;

    @Min(1)
    private int playerPlacement;

    public int getPlayerPlacement() {
        return playerPlacement;
    }

    public void setPlayerPlacement(int playerPlacement) {
        this.playerPlacement = playerPlacement;
    }

    public UserDTO getPlayer() {
        return player;
    }
    public void setPlayer(UserDTO player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TournamentRankingDTO)) return false;

        TournamentRankingDTO that = (TournamentRankingDTO) o;

        if (getPlayerPlacement() != that.getPlayerPlacement()) return false;
        return getPlayer() != null ? getPlayer().equals(that.getPlayer()) : that.getPlayer() == null;
    }

    @Override
    public int hashCode() {
        int result = getPlayer() != null ? getPlayer().hashCode() : 0;
        result = 31 * result + getPlayerPlacement();
        return result;
    }
}
