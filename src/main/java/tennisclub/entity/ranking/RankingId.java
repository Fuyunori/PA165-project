package tennisclub.entity.ranking;

import tennisclub.entity.Tournament;
import tennisclub.entity.User;

import java.io.Serializable;

/**
 * @author Ondrej Holub
 */
public class RankingId implements Serializable {

    private Tournament tournament;

    private User player;

    public RankingId(Tournament tournament, User player) {
        this.tournament = tournament;
        this.player = player;
    }
}
