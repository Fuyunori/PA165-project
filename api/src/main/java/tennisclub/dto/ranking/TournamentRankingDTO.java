package tennisclub.dto.ranking;

import javax.validation.constraints.Min;

public class TournamentRankingDTO {

//    Uncomment when UserDTO is added
//    @NotNull
//    private UserDTO player;

    @Min(1)
    private int playerPlacement;

    public int getPlayerPlacement() {
        return playerPlacement;
    }

    public void setPlayerPlacement(int playerPlacement) {
        this.playerPlacement = playerPlacement;
    }


//    Uncomment when UserDTO is added
//    public int getPlayer() {
//        return player;
//    }
//    public void setPlayer(User player) {
//        this.player = player;
//    }

    // TODO equals & hashcode when UserDTO is added


}
