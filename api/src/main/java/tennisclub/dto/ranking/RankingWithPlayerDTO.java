package tennisclub.dto.ranking;


import tennisclub.dto.user.UserDTO;

import javax.validation.constraints.NotNull;

/**
 * Ranking DTO describing the ranking from the perspective of a tournament
 * @author Ondrej Holub
 */
public class RankingWithPlayerDTO {

    @NotNull(message = "{ranking.player.notNull}")
    private UserDTO player;

    private Integer playerPlacement;

    public Integer getPlayerPlacement() {
        return playerPlacement;
    }

    public void setPlayerPlacement(Integer playerPlacement) {
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
        if (!(o instanceof RankingWithPlayerDTO)) return false;

        RankingWithPlayerDTO that = (RankingWithPlayerDTO) o;

        return getPlayer().equals(that.getPlayer());
    }

    @Override
    public int hashCode() {
        return getPlayer().hashCode();
    }
}
